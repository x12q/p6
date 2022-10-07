package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateRequest
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.common_data_structure.IndCellDM
import com.qxdzbc.p6.ui.app.state.StateContainer
import javax.inject.Inject

class EndThumbDragActionImp @Inject constructor(
    @StateContainerSt
    val stateContainerSt: St<@JvmSuppressWildcards StateContainer>,
    private val copyCellAct: CopyCellAction,
    private val multiCellUpdateAct: MultiCellUpdateAction,
) : EndThumbDragAction {

    val sc by stateContainerSt

    override fun invokeSuitableAction(wbws: WbWsSt, startCell: CellAddress, endCell: CellAddress, isCtrPressed: Boolean) {
        if(startCell!=endCell){
            val currentStartCellValue:Any? = sc.getCell(wbws,startCell)?.currentValue
            val isStartCellNumeric:Boolean = currentStartCellValue is Number
            if (isCtrPressed && isStartCellNumeric) {
                val startNum = (currentStartCellValue as Number).toDouble()
                generateNumberSequenceAndPutInCells(wbws, startNum,startCell, endCell)
            } else {
                copyContent(wbws, startCell, endCell)
            }
        }
    }

    fun generateNumberSequenceAndPutInCells(
        wbws: WbWsSt,
        startNum:Double,
        startCell: CellAddress,
        endCell: CellAddress,
    ) {
        var onRow = true
        val targetCells:List<CellAddress> = if(startCell.colIndex != endCell.colIndex){
            onRow = false
            startCell.generateCellSequenceToCol(endCell.colIndex)
        }else{
            startCell.generateCellSequenceToRow(endCell.rowIndex)
        }.filter { it != startCell }

        val updateEntries = targetCells.map{
            IndCellDM(
                address = it,
                content = CellContentDM(
                    cellValue = CellValue.from(startNum + if (onRow) it.rowIndex - startCell.rowIndex else it.colIndex - startCell.colIndex)
                )
            )
        }

        val updateRequest = MultiCellUpdateRequest(
            wbKeySt = wbws.wbKeySt,
            wsNameSt = wbws.wsNameSt,
            cellUpdateList = updateEntries
        )
        multiCellUpdateAct.updateMultiCell(updateRequest,true)
    }

    fun copyContent(wbws: WbWsSt, startCell: CellAddress, endCell: CellAddress) {

    }
}
