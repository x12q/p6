package com.qxdzbc.p6.app.action.worksheet.delete_multi

import com.github.michaelbull.result.*
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellRequest
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlin.collections.fold

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class DeleteMultiCellActionImp @Inject constructor(
    private val docCont: DocumentContainer,
    private val multiCellUpdateAct: UpdateMultiCellAction,
    private val stateCont: StateContainer,
    private val errorRouter: ErrorRouter,
) : DeleteMultiCellAction {

    private val sc = stateCont
    private val dc = docCont

    override fun deleteDataOfMultiCellAtCursor(request: DeleteMultiCellAtCursorRequest): RseNav<RemoveMultiCellResponse> {
        createCommandAtCursor(request)
        return internalApplyAtCursor(request)
    }

    override fun deleteDataOfMultiCell(
        request: DeleteMultiCellRequest,
        undoable: Boolean
    ): RseNav<RemoveMultiCellResponse> {
        if (undoable) {
            createCommandToDeleteMultiCell(request)
        }
        return internalApply(request)
    }

    private fun internalApplyAtCursor(request: DeleteMultiCellAtCursorRequest): RseNav<RemoveMultiCellResponse> {
        val response = makeRequestDeleteMultiCellAtCursor(request)
        apply(response)
        return response
    }

    private fun internalApply(request: DeleteMultiCellRequest): RseNav<RemoveMultiCellResponse> {
        val response = makeRequestDeleteMultiCell(request)
        apply(response)
        return response
    }

    private fun createCommandAtCursor(request: DeleteMultiCellAtCursorRequest) {
        val k = request.wbKey
        val n = request.wsName
        val ws = dc.getWs(k, n)
        if (ws != null) {
            val cursorState: CursorState? = sc.getCursorState(k, n)
            if (cursorState != null) {
                createCommandToDeleteMultiCell(
                    DeleteMultiCellRequest(
                        ranges = cursorState.allRanges,
                        cells = cursorState.allFragCells,
                        wbKey = request.wbKey,
                        wsName = request.wsName,
                        clearFormat = request.clearFormat,
                        windowId = request.windowId
                    )
                )
            }
        }
    }

    fun apply(res: RseNav<RemoveMultiCellResponse>): RseNav<RemoveMultiCellResponse> {
        res.onFailure { err ->
            errorRouter.publish(err)
        }
        return res
    }


    private fun createCommandToDeleteMultiCell(request: DeleteMultiCellRequest) {
        val wbKey = request.wbKey
        val wsName = request.wsName
        val ws = dc.getWs(wbKey, wsName)
        if (ws != null) {
            val command = object : BaseCommand() {
                val _originalRequest = request
                val _wbKeySt = ws.wbKeySt
                val _wsNameSt = ws.wsNameSt

                val updateList: List<IndependentCellDM> = run {
                    val _allCellAddresses: Set<CellAddress> = request.ranges.fold(setOf<CellAddress>()) { acc, range ->
                        val addressesFromWs = ws.getCellsInRange(range).map { it.address }
                        acc + addressesFromWs
                    } + request.cells

                    _allCellAddresses.mapNotNull {
                        val cell = ws.getCell(it)
                        if (cell != null) {
                            IndependentCellDM(
                                address = it,
                                content = cell.content.toDm()
                            )
                        } else {
                            null
                        }
                    }
                }

                override fun run() {
                    /*
                    need to re-create the request every time the command is run because
                    the target Workbook key and Worksheet name might have changed
                     */
                    val _runRequest = _originalRequest.copy(
                        wbKey = _wbKeySt.value,
                        wsName = _wsNameSt.value,
                    )
                    internalApply(_runRequest)
                }

                override fun undo() {
                    val undoRequest = UpdateMultiCellRequest(
                        wbKeySt = _wbKeySt,
                        wsNameSt = _wsNameSt,
                        cellUpdateList = updateList
                    )
                    multiCellUpdateAct.updateMultiCell(undoRequest, true)
                }
            }

            sc.getUndoStackMs(WbWs(wbKey, wsName))?.also { commandStackMs ->
                commandStackMs.value = commandStackMs.value.add(command)
            }

        }
    }

    fun makeRequestDeleteMultiCellAtCursor(request: DeleteMultiCellAtCursorRequest): RseNav<RemoveMultiCellResponse> {
        val rangeCells = stateCont.getWsStateRs(request.wbKey, request.wsName).map { wsState ->
            val ranges = wsState.cursorState.allRanges
            val cells = wsState.cursorState.allFragCells
            (ranges to cells)
        }
        val ranges = rangeCells.component1()?.first ?: emptyList()
        val cells = rangeCells.component1()?.second ?: emptyList()
        val q = makeRequestDeleteMultiCell(
            DeleteMultiCellRequest(
                ranges = ranges,
                cells = cells,
                wbKey = request.wbKey,
                wsName = request.wsName,
                clearFormat = request.clearFormat,
                windowId = request.windowId
            )
        )
        return q
    }

    fun makeRequestDeleteMultiCell(request: DeleteMultiCellRequest): RseNav<RemoveMultiCellResponse> {
        val wbk = request.wbKey
        val wsn = request.wsName
        val rt = stateCont.getWbRs(wbk).flatMap { wb ->
            wb.getWsRs(wsn).flatMap { ws ->
                val ranges: List<RangeAddress> = request.ranges
                val cells: List<CellAddress> = request.cells
                // x: remove cell from worksheet
                ws.removeCells(cells)
                val cellsInRanges = ranges
                    .flatMap { ws.getCellsInRange(it) }
                    .map { it.address }
                    .toSet()
                ws.removeCells(cellsInRanges)
                wb.apply {
                    reRun()
                }
                val oldWsState = stateCont.getWsState(wbk, wsn)
                if (request.clearFormat) {
                    oldWsState?.removeCellState(cells + cellsInRanges)
                }
                oldWsState?.refreshCellState()
                Ok(
                    RemoveMultiCellResponse(
                        newWb = wb,
                        newWsState = oldWsState
                    )
                )
            }
        }
        return rt.mapError { it.withNav(request) }
    }


}
