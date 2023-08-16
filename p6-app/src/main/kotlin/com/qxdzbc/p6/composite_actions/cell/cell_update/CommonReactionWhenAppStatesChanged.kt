package com.qxdzbc.p6.composite_actions.cell.cell_update

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.composite_actions.cell.multi_cell_update.UpdateMultiCellRequest
import com.qxdzbc.p6.composite_actions.cell.multi_cell_update.UpdateMultiCellRequestDM
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.composite_actions.cursor.on_cursor_changed_reactor.CommonSideEffectWhenCursorChanged
import com.qxdzbc.p6.document_data_layer.cell.CellId
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM

/**
 * TODO This is not appropriate, don't expand this anymore. These reaction should be handled directly by the corresponding action objects.
 */
interface CommonReactionWhenAppStatesChanged : CommonSideEffectWhenCursorChanged {
    fun onOneCellChanged(request: CellUpdateRequest)
    fun onOneCellChanged(request: CellUpdateRequestDM)
    fun onOneCellChanged(cellId:CellId)
    fun onOneCellChanged(cellId: CellIdDM)

    fun onUpdateMultipleCells(request: UpdateMultiCellRequestDM)
    fun onUpdateMultipleCells(request: UpdateMultiCellRequest)

    fun onUpdateMultipleCells(cellIds:List<CellId>)
    fun onUpdateMultipleCellsDM(cellIds:List<CellIdDM>)

    fun onWbChange(wbKeySt:St<WorkbookKey>)
    fun onWbChange(wbKey:WorkbookKey)

    fun onWsChanged(wbwsSt:WbWsSt)
    fun onWsChanged(wbws: WbWs)
}
