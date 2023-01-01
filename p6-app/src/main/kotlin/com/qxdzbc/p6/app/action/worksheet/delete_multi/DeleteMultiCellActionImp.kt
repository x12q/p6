package com.qxdzbc.p6.app.action.worksheet.delete_multi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellRequest
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.worksheet.delete_multi.applier.DeleteMultiApplier
import com.qxdzbc.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRM
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.rpc.common_data_structure.IndCellDM
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
    private val appStateMs: Ms<AppState>,
    private val docContMs: Ms<DocumentContainer>,
    private val multiCellUpdateAct:UpdateMultiCellAction,
    val stateContMs: Ms<SubAppStateContainer>,
) : DeleteMultiCellAction {

    private var sc by stateContMs
    private var appState by appStateMs
    private var dc by docContMs

    override fun deleteMultiCellAtCursor(request: DeleteMultiAtCursorRequest): RseNav<RemoveMultiCellResponse> {
        createCommandAtCursor(request)
        return internalApplyAtCursor(request)
    }

    override fun deleteMultiCell(request: RemoveMultiCellRequest,undoable:Boolean): RseNav<RemoveMultiCellResponse> {
        if(undoable){
            createDeleteMultiCellCommand(request)
        }
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
        val ws = dc.getWs(k, n)
        if (ws != null) {
            val cursorState: CursorState? = sc.getCursorState(k, n)
            if (cursorState != null) {
                createDeleteMultiCellCommand(
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


    private fun createDeleteMultiCellCommand(request: RemoveMultiCellRequest) {
        val wbKey = request.wbKey
        val wsName = request.wsName
        val ws = dc.getWs(wbKey, wsName)
        if (ws != null) {
            val command = object : BaseCommand() {
                val _request = request
                val _wbWsSt = WbWsSt(ws.wbKeySt,ws.wsNameSt)
                val allCellAddresses: Set<CellAddress> = request.ranges.fold(setOf<CellAddress>()) { acc, range ->
                    val addressesFromWs = ws.getCellsInRange(range).map { it.address }
                    acc + addressesFromWs
                } + request.cells
                val updateList: List<IndCellDM> = allCellAddresses.mapNotNull {
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

                val _oRequest get()=_request.copy(
                    wbKey = _wbWsSt.wbKey,
                    wsName = _wbWsSt.wsName,
                )

                override fun run() {
                    internalApply(_oRequest)
                }

                override fun undo() {
                    // need to implement multi update request
                    val cellMultiUpdateRequest = UpdateMultiCellRequest(
                        wbKeySt = _wbWsSt.wbKeySt,
                        wsNameSt = _wbWsSt.wsNameSt,
                        cellUpdateList = updateList
                    )
                    multiCellUpdateAct.updateMultiCell(cellMultiUpdateRequest,true)
                }
            }

                sc.getUndoStackMs(WbWs(wbKey,wsName))?.also { commandStackMs->
                    commandStackMs.value = commandStackMs.value.add(command)
                }

        }
    }
}
