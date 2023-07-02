package com.qxdzbc.p6.app.action.app.load_wb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.path.PPath
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp.Companion.toShallowModel
import com.qxdzbc.p6.app.file.loader.P6FileLoader
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto

import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.file.P6FileLoaderErrors
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.CellFormatTable.Companion.toModel
import com.squareup.anvil.annotations.ContributesBinding
import java.util.*
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
data class LoadWorkbookActionImp @Inject constructor(
    val stateCont:StateContainer,
    val errorRouter: ErrorRouter,
    private val loader: P6FileLoader,
    val translatorContainer: TranslatorContainer,
) : LoadWorkbookAction {

    private val sc = stateCont
    private val tc = translatorContainer
    private var wbCont by sc.wbContMs
    private var wbStateCont by sc.wbStateContMs

    override fun loadWorkbook(request: LoadWorkbookRequest): LoadWorkbookResponse {
        val path: PPath = request.path
        val windowId: String? = request.windowId
        val windowStateMs = sc.getWindowStateMsDefaultRs(windowId)
        if (path.exists() && path.isRegularFile()) {
            if (path.isReadable()) {
                val res = loadWb(request)
                applyResponse(res.first, res.second)
                return res.first
            } else {
                val e = P6FileLoaderErrors.notReadableFile(path)
                if (windowStateMs is Ok) {
                    windowStateMs.value.value.publishError(e)
                } else {
                    errorRouter.publishToApp(e)
                }
                return LoadWorkbookResponse(
                    errorReport = e,
                    windowId = request.windowId,
                    wb = null
                )
            }
        } else {
            val e = P6FileLoaderErrors.notAFile(path)
            if (windowStateMs is Ok) {
                windowStateMs.value.value.publishError(e)
            } else {
                errorRouter.publishToApp(e)
            }
            return LoadWorkbookResponse(
                errorReport = e,
                windowId = request.windowId,
                wb = null
            )
        }
    }

    /**
     * Read data from file, convert it to appropriate format and return it
     */
    fun loadWb(request: LoadWorkbookRequest): Pair<LoadWorkbookResponse, WorkbookProto?> {
        val loadRs = loader.load3Rs(request.path.path)
        when (loadRs) {
            is Ok -> {
                val wb = loadRs.value.toShallowModel(tc::getTranslatorOrCreate)
                return LoadWorkbookResponse(
                    windowId = request.windowId,
                    errorReport = null,
                    wb = wb
                ) to loadRs.value
            }

            is Err -> {
                return LoadWorkbookResponse(
                    windowId = request.windowId,
                    errorReport = loadRs.error,
                    wb = null
                ) to null
            }
        }
    }

    /**
     * Apply loaded data to the app state
     */
    fun applyResponse(res: LoadWorkbookResponse?, proto: WorkbookProto?) {
        if (res != null) {
            val err = res.errorReport
            if (err != null) {
                errorRouter.publishToWindow(err, res.windowId, res.wbKey)
            } else {
                val cellFormatTableMap = proto?.worksheetList?.associate { wsProto ->
                    wsProto.name to wsProto.cellFormatTable.toModel()
                }

                val colWidthMapByWsName:Map<String,Map<Int,Int>>? = proto?.worksheetList?.associate {wsProto->
                    wsProto.name to wsProto.columnWidthMapMap
                }

                val rowHeightByWsName = proto?.worksheetList?.associate {wsProto->
                    wsProto.name to wsProto.rowHeightMapMap
                }

                apply(res.windowId, res.wb, cellFormatTableMap,colWidthMapByWsName,rowHeightByWsName)
            }
        }
    }

    /**
     * Apply loaded data to the app state
     */
    fun apply(
        windowId: String?,
        workbook: Workbook?,
        cellFormatTableMap:Map<String,CellFormatTable>?,
        colWidthMapByWsName:Map<String,Map<Int,Int>>?,
        rowHeightByWsName:Map<String,Map<Int,Int>>?,
    ) {

        val windowStateMsRs = sc.getWindowStateMsDefaultRs(windowId)
        workbook?.also {
            wbCont = wbCont.addOrOverWriteWb(workbook)
            when (windowStateMsRs) {
                is Ok -> {
                    val windowStateMs = windowStateMsRs.value
                    val wbk = workbook.key
                    val wbkMs = workbook.keyMs
                    wbStateCont.getWbStateMs(wbk)?.also {
                        it.value = it.value.setWindowId(windowId).setNeedSave(false)
                    }
                    windowStateMs.value = windowStateMs.value.addWbKey(wbkMs)
                    windowStateMs.value.activeWbPointerMs.value  = windowStateMs.value.activeWbPointerMs.value.pointTo(wbkMs)
                }

                is Err -> {
                    // x: designated window does not exist and can't get a default window state => create a new window for the loaded workbook with the provided window id or a new random window id
                    val newWindowId = windowId ?: UUID.randomUUID().toString()
                    val newOuterWindowStateMs = sc.createNewWindowStateMs(newWindowId)
                    val newWindowStateMs = newOuterWindowStateMs.value.innerWindowStateMs
                    wbStateCont.getWbStateMs(workbook.key)?.also {
                        it.value = it.value.setWindowId(newWindowId).setNeedSave(false)
                        newWindowStateMs.value.activeWbPointerMs.value =
                            newWindowStateMs.value.activeWbPointer.pointTo(it.value.wbKeyMs)
                    }
                    val s2 = newWindowStateMs.value.addWbKey(workbook.keyMs)
                    newWindowStateMs.value = s2
                }
            }

            cellFormatTableMap?.forEach { (wsName, cellFormatTable) ->
                sc.getCellFormatTableMs(WbWs(workbook.key, wsName))?.also {
                    it.value = cellFormatTable
                }
            }

            colWidthMapByWsName?.forEach { (wsName, colWidthMap) ->
                val wbws = WbWs(workbook.key,wsName)
                sc.getRulerStateMs(wbws, RulerType.Col)?.also {
                    it.value =it.value.setMultiItemSize(colWidthMap.mapValues { it.value.dp })
                }
            }

            rowHeightByWsName?.forEach{(wsName,rowHeightMap)->
                val wbws = WbWs(workbook.key,wsName)
                sc.getRulerStateMs(wbws,RulerType.Row)?.also{
                    it.value = it.value.setMultiItemSize(rowHeightMap.mapValues { it.value.dp })
                }
            }
        }
    }
}
