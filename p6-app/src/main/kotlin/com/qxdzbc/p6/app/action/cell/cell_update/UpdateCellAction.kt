package com.qxdzbc.p6.app.action.cell.cell_update

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM

/**
 * Update a single cell with input from either a [CellUpdateRequestDM] or [CellContentDM] or [CellUpdateRequest]
 */
interface UpdateCellAction {
    /**
     * An indirect update function, use raw data and perform state lookup underneath to find the state to update. Slower than direct method, but more straightforward to use and is used in RPC server.
     * [publishError] means whether to copy the err object to respective error container or not.
     */
    fun updateCellDM(request: CellUpdateRequestDM, publishError: Boolean = true): Rse<Unit>

    fun updateCellDM(
        cellId: CellIdDM,
        cellContent: CellContentDM,
        publishError: Boolean = true
    ): Rse<Unit>

    /**
     * Direct update function, use state reference to directly get the state object to update
     */
    fun updateCell(request: CellUpdateRequest, publishError: Boolean = true): Rse<Unit>
}
