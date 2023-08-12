package com.qxdzbc.p6.composite_actions.app.load_wb

import androidx.compose.ui.unit.dp
import com.github.michaelbull.result.*
import com.qxdzbc.common.Rse
import com.qxdzbc.common.path.PPath
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookImp.Companion.toShallowModel
import com.qxdzbc.p6.file.loader.P6FileLoader
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto

import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.qxdzbc.p6.ui.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.file.P6FileLoaderErrors
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.CellFormatTable.Companion.toModel
import com.qxdzbc.p6.ui.window.state.WindowState
import com.squareup.anvil.annotations.ContributesBinding
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
data class LoadWorkbookActionImp @Inject constructor(
    val stateCont: StateContainer,
    val errorRouter: ErrorRouter,
    val loader: P6FileLoader,
    val translatorContainer: TranslatorContainer,
) : LoadWorkbookAction {

    private val sc = stateCont
    private val tc = translatorContainer
    private val wbCont = sc.wbCont
    private val wbStateCont = sc.wbStateCont

    override fun loadWorkbook(request: LoadWorkbookRequest): LoadWorkbookResponse {
        val path: PPath = request.path
        val windowId: String? = request.windowId
        val windowStateRs: Rse<WindowState> = sc.getWindowState_OrDefault_Rs(windowId)
        if (path.exists() && path.isRegularFile()) {
            if (path.isReadable()) {
                val loadRs = loadWb(request)
                applyLoadResult(request, loadRs)
                val rt = loadRs
                    .mapBoth(
                        success = {
                            LoadWorkbookResponse(
                                errorReport = null,
                                windowId = request.windowId,
                                wb = it.first
                            )
                        },
                        failure = {
                            LoadWorkbookResponse(
                                errorReport = it,
                                windowId = request.windowId,
                                wb = null
                            )
                        }
                    )
                return rt
            } else {
                val e = P6FileLoaderErrors.notReadableFile(path)
                errorRouter.publishToWindow(e, windowStateRs.component1()?.id)
                return LoadWorkbookResponse(
                    errorReport = e,
                    windowId = request.windowId,
                    wb = null
                )
            }
        } else {
            val err = P6FileLoaderErrors.notAFile(path)
            errorRouter.publishToWindow(err, windowStateRs.component1()?.id)
            return LoadWorkbookResponse(
                errorReport = err,
                windowId = request.windowId,
                wb = null
            )
        }
    }

    fun loadWb(request: LoadWorkbookRequest): Rse<Pair<Workbook, WorkbookProto>> {
        val loadRs = loader.load3Rs(request.path.path)
        val rt = loadRs.map { proto ->
            val wb = proto.toShallowModel(tc::getTranslatorOrCreate)
            wb to proto
        }
        return rt
    }

    fun applyLoadResult(request: LoadWorkbookRequest, rs: Rse<Pair<Workbook, WorkbookProto>>) {
        rs.onSuccess { (wb, proto) ->
            val cellFormatTableMap = proto?.worksheetList?.associate { wsProto ->
                wsProto.name to wsProto.cellFormatTable.toModel()
            }

            val colWidthMapByWsName: Map<String, Map<Int, Int>>? = proto?.worksheetList?.associate { wsProto ->
                wsProto.name to wsProto.columnWidthMapMap
            }

            val rowHeightByWsName = proto?.worksheetList?.associate { wsProto ->
                wsProto.name to wsProto.rowHeightMapMap
            }

            apply(request.windowId, wb, cellFormatTableMap, colWidthMapByWsName, rowHeightByWsName)
                .onFailure { err->
                    errorRouter.publishToWindow(err,request.windowId)
                }
        }.onFailure { err ->
            errorRouter.publishToWindow(err, request.windowId)
        }
    }

    fun apply(
        windowId: String?,
        workbook: Workbook?,
        cellFormatTableMap: Map<String, CellFormatTable>?,
        colWidthMapByWsName: Map<String, Map<Int, Int>>?,
        rowHeightByWsName: Map<String, Map<Int, Int>>?,
    ):Rse<Unit> {

        if (workbook != null) {
            val addWbRs = wbCont.addWbRs(workbook)
            val rt = addWbRs
                .onSuccess {
                    val windowStateRs = sc.getWindowState_OrDefault_Rs(windowId)
                    when (windowStateRs) {
                        is Ok -> {
                            val windowState = windowStateRs.value
                            val wbk = workbook.key
                            val wbkMs = workbook.keyMs
                            wbStateCont.getWbState(wbk)?.also {
                                it.let {
                                    it.windowId = windowId
                                    it.needSave = false
                                }
                            }
                            windowState.addWbKey(wbkMs)
                            windowState.activeWbPointerMs.value = windowState.activeWbPointerMs.value.pointTo(wbkMs)
                        }

                        is Err -> {
                            // x: designated window does not exist and can't get a default window state => create a new window for the loaded workbook with the provided window id or a new random window id
                            val newWindowId = windowId ?: UUID.randomUUID().toString()
                            val newOuterWindowStateMs = sc.createNewWindowStateMs(newWindowId)
                            val newWindowStateMs = newOuterWindowStateMs.value.innerWindowState
                            wbStateCont.getWbState(workbook.key)?.also {
                                it.let {
                                    it.windowId = newWindowId
                                    it.needSave = false
                                }
                                newWindowStateMs.activeWbPointerMs.value =
                                    newWindowStateMs.activeWbPointer.pointTo(it.wbKeyMs)
                            }
                            newWindowStateMs.addWbKey(workbook.keyMs)
                        }
                    }

                    /*
                     the 3 blocks below apply the format (cell format, col with, row height)
                     to format tables and ruler state of target wb in StateContainer
                     */

                    cellFormatTableMap?.forEach { (wsName, cellFormatTable) ->
                        sc.getCellFormatTableMs(WbWs(workbook.key, wsName))
                            ?.also {
                                it.value = cellFormatTable
                            }
                    }

                    colWidthMapByWsName?.forEach { (wsName, colWidthMap) ->
                        sc.getRulerStateMs(WbWs(workbook.key, wsName), RulerType.Col)
                            ?.also {
                                it.value = it.value.setMultiItemSize(colWidthMap.mapValues { it.value.dp })
                            }
                    }

                    rowHeightByWsName?.forEach { (wsName, rowHeightMap) ->
                        sc.getRulerStateMs(WbWs(workbook.key, wsName), RulerType.Row)
                            ?.also {
                                it.value = it.value.setMultiItemSize(rowHeightMap.mapValues { it.value.dp })
                            }
                    }
                }

            return rt
        } else {
            return Err(P6FileLoaderErrors.nullWorkboook)
        }
    }

    /**
     * Apply loaded data to the app state
     */
//    fun apply(
//        windowId: String?,
//        workbook: Workbook?,
//        cellFormatTableMap: Map<String, CellFormatTable>?,
//        colWidthMapByWsName: Map<String, Map<Int, Int>>?,
//        rowHeightByWsName: Map<String, Map<Int, Int>>?,
//    ) {
//
//        /**
//         * When I read a file, I construct a new Workbook. This new Workbook contains a set of distinct Ms within itself. So, it is reasonable to keep the Ms layer. But. Should My logic rely on overwriting wb function?
//         * I rely on the overwrite function because I want to allow loading a file overwritting existing workbook. This behavior is dangerous because it could overwrite existing data. => remove this behavior.
//         */
//        val windowStateMsRs = sc.getWindowState_OrDefault_Rs(windowId)
//        workbook?.also {
//            wbCont.addOrOverWriteWb(workbook)
//            when (windowStateMsRs) {
//                is Ok -> {
//                    val windowState = windowStateMsRs.value
//                    val wbk = workbook.key
//                    val wbkMs = workbook.keyMs
//                    wbStateCont.getWbState(wbk)?.also {
//                        it.let {
//                            it.windowId = windowId
//                            it.needSave = false
//                        }
//                    }
//                    windowState.addWbKey(wbkMs)
//                    windowState.activeWbPointerMs.value = windowState.activeWbPointerMs.value.pointTo(wbkMs)
//                }
//
//                is Err -> {
//                    // x: designated window does not exist and can't get a default window state => create a new window for the loaded workbook with the provided window id or a new random window id
//                    val newWindowId = windowId ?: UUID.randomUUID().toString()
//                    val newOuterWindowStateMs = sc.createNewWindowStateMs(newWindowId)
//                    val newWindowStateMs = newOuterWindowStateMs.value.innerWindowState
//                    wbStateCont.getWbState(workbook.key)?.also {
//                        it.let {
//                            it.windowId = newWindowId
//                            it.needSave = false
//                        }
//                        newWindowStateMs.activeWbPointerMs.value =
//                            newWindowStateMs.activeWbPointer.pointTo(it.wbKeyMs)
//                    }
//                    newWindowStateMs.addWbKey(workbook.keyMs)
//                }
//            }
//
//            /*
//             the 3 blocks below apply the format (cell format, col with, row height)
//             to format tables and ruler state of target wb in StateContainer
//             */
//
//            cellFormatTableMap?.forEach { (wsName, cellFormatTable) ->
//                sc.getCellFormatTableMs(WbWs(workbook.key, wsName))
//                    ?.also {
//                        it.value = cellFormatTable
//                    }
//            }
//
//            colWidthMapByWsName?.forEach { (wsName, colWidthMap) ->
//                sc.getRulerStateMs(WbWs(workbook.key, wsName), RulerType.Col)
//                    ?.also {
//                        it.value = it.value.setMultiItemSize(colWidthMap.mapValues { it.value.dp })
//                    }
//            }
//
//            rowHeightByWsName?.forEach { (wsName, rowHeightMap) ->
//                sc.getRulerStateMs(WbWs(workbook.key, wsName), RulerType.Row)
//                    ?.also {
//                        it.value = it.value.setMultiItemSize(rowHeightMap.mapValues { it.value.dp })
//                    }
//            }
//        }
//    }
}
