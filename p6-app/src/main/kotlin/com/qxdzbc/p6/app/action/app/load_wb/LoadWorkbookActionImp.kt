package com.qxdzbc.p6.app.action.app.load_wb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
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
    private val wbStateCont = sc.wbStateCont

    override fun loadWorkbook(request: LoadWorkbookRequest): LoadWorkbookResponse {
        val path: PPath = request.path
        val windowId: String? = request.windowId
        val windowStateMsRs = sc.getWindowStateMs_OrDefault_OrCreateANewOne_Rs(windowId)
        if (path.exists() && path.isRegularFile()) {
            if (path.isReadable()) {
                val res = loadWb(request)
                applyResponse(res.first, res.second)
                return res.first
            } else {
                val e = P6FileLoaderErrors.notReadableFile(path)
                errorRouter.publishToWindow(e,windowStateMsRs.component1()?.id)
                return LoadWorkbookResponse(
                    errorReport = e,
                    windowId = request.windowId,
                    wb = null
                )
            }
        } else {
            val e = P6FileLoaderErrors.notAFile(path)
            errorRouter.publishToWindow(e,windowStateMsRs.component1()?.id)
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

        val windowStateMsRs = sc.getWindowStateMs_OrDefault_OrCreateANewOne_Rs(windowId)
        workbook?.also {
            wbCont = wbCont.addOrOverWriteWb(workbook)
            when (windowStateMsRs) {
                is Ok -> {
                    val windowState = windowStateMsRs.value
                    val wbk = workbook.key
                    val wbkMs = workbook.keyMs
                    wbStateCont.getWbState(wbk)?.also {
                        it.let{
                            it.windowId = windowId
                            it.needSave = false
                        }
                    }
                    windowState.addWbKey(wbkMs)
                    windowState.activeWbPointerMs.value  = windowState.activeWbPointerMs.value.pointTo(wbkMs)
                }

                is Err -> {
                    // x: designated window does not exist and can't get a default window state => create a new window for the loaded workbook with the provided window id or a new random window id
                    val newWindowId = windowId ?: UUID.randomUUID().toString()
                    val newOuterWindowStateMs = sc.createNewWindowStateMs(newWindowId)
                    val newWindowStateMs = newOuterWindowStateMs.value.innerWindowState
                    wbStateCont.getWbState(workbook.key)?.also {
                        it.let{
                            it.windowId = newWindowId
                            it.needSave = false
                        }
                        newWindowStateMs.activeWbPointerMs.value =
                            newWindowStateMs.activeWbPointer.pointTo(it.wbKeyMs)
                    }
                    newWindowStateMs.addWbKey(workbook.keyMs)
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
