package com.emeraldblast.p6.app.action.worksheet.delete_multi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.applier.WorkbookUpdateCommonApplier
import com.emeraldblast.p6.app.action.worksheet.delete_multi.applier.DeleteMultiApplier
import com.emeraldblast.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRM
import com.emeraldblast.p6.app.action.worksheet.update_multi_cell.rm.UpdateMultiCellRM
import com.emeraldblast.p6.app.command.BaseCommand
import com.emeraldblast.p6.app.common.utils.RseNav
import com.emeraldblast.p6.app.action.cell.cell_multi_update.CellMultiUpdateRequest
import com.emeraldblast.p6.app.action.cell.cell_multi_update.CellUpdateContent
import com.emeraldblast.p6.app.action.cell.cell_multi_update.CellUpdateEntry
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import javax.inject.Inject

class DeleteMultiActionImp @Inject constructor(
    val rm:DeleteMultiRM,
    val applier:DeleteMultiApplier,
    @AppStateMs
    private val appStateMs:Ms<AppState>,
    private val cellUpdateRM: UpdateMultiCellRM,
    private val wbUpdateApplier: WorkbookUpdateCommonApplier,
) : DeleteMultiAction {

    private var appState by appStateMs

    override fun deleteMulti2(request: DeleteMultiRequest2): RseNav<DeleteMultiResponse2> {
        createCommand(request)
        return internalApply(request)
    }

    private fun internalApply(request: DeleteMultiRequest2): RseNav<DeleteMultiResponse2> {
        val response = rm.deleteMulti2(request)
        applier.apply(response)
        return response
    }

    private fun createCommand(request: DeleteMultiRequest2){
        val k = request.wbKey
        val n = request.wsName
        val ws = appState.getWorksheet(k,n)
        if (ws != null) {
            val cursorState = appState.getCursorState(k,n)
            if(cursorState!=null){
                val command = object : BaseCommand() {
                    val allCell: Set<CellAddress> = cursorState.allRanges.fold(setOf<CellAddress>()) { acc, range ->
                        val z = ws.getCellsInRange(range).map { it.address }
                        acc + z
                    } + cursorState.allFragCells
                    val updateList: List<CellUpdateEntry> = allCell.mapNotNull {
                        val cell = ws.getCellOrNull(it)
                        if (cell != null) {
                            CellUpdateEntry(
                                cellAddress = it,
                                cellUpdateContent = CellUpdateContent(
                                    formula = cell.formula ?: "",
                                    displayValue = cell.displayValue,
                                    cellValue = cell.cellValueAfterRun.valueAfterRun
                                )
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
                        val cellMultiUpdateRequest = CellMultiUpdateRequest(
                            wbKey = cursorState.id.wbKey,
                            wsName = cursorState.id.wsName,
                            cellUpdateList = updateList
                        )
                        val r = cellUpdateRM.cellMultiUpdate(cellMultiUpdateRequest)
                        if (r != null) {
                            wbUpdateApplier.apply(r)
                        }
                    }
                }
                appState.queryStateByWorkbookKey(k).ifOk {
                    val commandStackMs = it.workbookState.commandStackMs
                    commandStackMs.value = commandStackMs.value.add(command)
                }

            }

        }
    }
}
