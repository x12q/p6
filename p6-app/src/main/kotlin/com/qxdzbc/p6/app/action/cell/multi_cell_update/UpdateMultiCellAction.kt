package com.qxdzbc.p6.app.action.cell.multi_cell_update

import com.qxdzbc.common.Rse

interface UpdateMultiCellAction {
    /**
     * this one is for rpc call, slower than the other
     */
    fun updateMultiCellDM(request:UpdateMultiCellRequestDM, publishErr:Boolean = true):Rse<Unit>
    fun updateMultiCell(request:UpdateMultiCellRequest, publishErr:Boolean = true):Rse<Unit>
}
