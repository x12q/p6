package com.qxdzbc.p6.app.action.worksheet.delete_multi

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellRequest
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.worksheet.delete_multi.applier.DeleteMultiApplier
import com.qxdzbc.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRM
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class DeleteMultiCellActionImp @Inject constructor(
    val rm: DeleteMultiRM,
    val applier: DeleteMultiApplier,
    private val docCont: DocumentContainer,
    private val multiCellUpdateAct: UpdateMultiCellAction,
    private val subAppStateContainer: SubAppStateContainer,
) : DeleteMultiCellAction {

    private val sc = subAppStateContainer
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
        val response = rm.deleteMultiCellAtCursor(request)
        applier.apply(response)
        return response
    }

    private fun internalApply(request: DeleteMultiCellRequest): RseNav<RemoveMultiCellResponse> {
        val response = rm.deleteMultiCell(request)
        applier.apply(response)
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
}
