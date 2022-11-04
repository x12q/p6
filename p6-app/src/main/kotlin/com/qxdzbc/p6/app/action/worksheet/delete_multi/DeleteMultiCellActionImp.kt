package com.qxdzbc.p6.app.action.worksheet.delete_multi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplier
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateRequest
import com.qxdzbc.p6.app.action.worksheet.delete_multi.applier.DeleteMultiApplier
import com.qxdzbc.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRM
import com.qxdzbc.p6.app.action.worksheet.update_multi_cell.rm.UpdateMultiCellRM
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.rpc.common_data_structure.IndCellDM
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class DeleteMultiCellActionImp @Inject constructor(
    val rm: DeleteMultiRM,
    val applier: DeleteMultiApplier,
    private val appStateMs: Ms<AppState>,
    private val multiCellUpdateAct:MultiCellUpdateAction,
) : DeleteMultiCellAction {

    private var appState by appStateMs

    override fun deleteMultiCellAtCursor(request: DeleteMultiAtCursorRequest): RseNav<RemoveMultiCellResponse> {
        createCommandAtCursor(request)
        return internalApplyAtCursor(request)
    }

    override fun deleteMultiCell(request: RemoveMultiCellRequest): RseNav<RemoveMultiCellResponse> {
        createCommand(request)
        return internalApply(request)
    }

    private fun internalApplyAtCursor(request: DeleteMultiAtCursorRequest): RseNav<RemoveMultiCellResponse> {
        val response = rm.deleteMultiCellAtCursor(request)
        applier.apply(response)
        return response
    }

    private fun internalApply(request: RemoveMultiCellRequest): RseNav<RemoveMultiCellResponse> {
        val response = rm.deleteMultiCell(request)
        applier.apply(response)
        return response
    }

    private fun createCommandAtCursor(request: DeleteMultiAtCursorRequest) {
        val k = request.wbKey
        val n = request.wsName
        val ws = appState.getWs(k, n)
        if (ws != null) {
            val cursorState: CursorState? = appState.getCursorState(k, n)
            if (cursorState != null) {
                createCommand(
                    RemoveMultiCellRequest(
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


    private fun createCommand(request: RemoveMultiCellRequest) {
        val k = request.wbKey
        val n = request.wsName
        val ws = appState.getWs(k, n)
        if (ws != null) {
            val command = object : BaseCommand() {
                val allCell: Set<CellAddress> = request.ranges.fold(setOf<CellAddress>()) { acc, range ->
                    val z = ws.getCellsInRange(range).map { it.address }
                    acc + z
                } + request.cells
                val updateList: List<IndCellDM> = allCell.mapNotNull {
                    val cell = ws.getCell(it)
                    if (cell != null) {
                        IndCellDM(
                            address = it,
                            content = cell.content.toDm()
                        )
                    } else {
                        null
                    }
                }

                override fun run() {
                    internalApply(request)
                }

                override fun undo() {
                    // need to implement multi update request
                    val cellMultiUpdateRequest = MultiCellUpdateRequest(
                        wbKeySt = ws.wbKeySt,
                        wsNameSt = ws.wsNameSt,
                        cellUpdateList = updateList
                    )
                    multiCellUpdateAct.updateMultiCell(cellMultiUpdateRequest,true)
                }
            }
            appState.queryStateByWorkbookKey(k).ifOk {
                val commandStackMs = it.workbookState.commandStackMs
                commandStackMs.value = commandStackMs.value.add(command)
            }
        }
    }
}
