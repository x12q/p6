package com.qxdzbc.p6.composite_actions.cursor.thumb.drag_thumb_action

import com.qxdzbc.p6.composite_actions.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.composite_actions.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.composite_actions.cell.multi_cell_update.UpdateMultiCellRequest
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.di.P6AnvilScope

import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest
import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class EndThumbDragActionImp @Inject constructor(
    val stateContainerSt:StateContainer,
    private val copyCellAct: CopyCellAction,
    private val multiCellUpdateAct: UpdateMultiCellAction,
) : EndThumbDragAction {

    val sc  = stateContainerSt

    /**
     * When release drag on ws cursor, depend on the content of the main cell of the cursor, do the following:
     * - if the main cell is numeric -> generate a sequence of numbers, and paste them into the selected cells.
     * - otherwise, copy the content of the main cell to the selected cells.
     */
    override fun onEndDrag(wbws: WbWsSt, startCell: CellAddress, endCell: CellAddress, isCtrPressed: Boolean) {
        if(startCell!=endCell){
            val cell = sc.getCellOrDefault(wbws,startCell)
            val currentStartCellValue:Any? = cell?.currentValue
            val startCellIsNumeric:Boolean = !(cell?.isFormula ?: false) && (currentStartCellValue is Number)
            if (isCtrPressed && startCellIsNumeric) {
                val startNum = (currentStartCellValue as Number).toDouble()
                generateNumberSequenceAndPutInCells(wbws, startNum,startCell, endCell)
            } else {
                copyContent(wbws, startCell, endCell)
            }
        }
    }

    /**
     * Generate a list of number starting from [startNum] with increase step = 1.
     *
     * Then paste the sequence of number into the range from [startCell] to [endCell].
     */
    private fun generateNumberSequenceAndPutInCells(
        wbws: WbWsSt,
        startNum:Double,
        startCell: CellAddress,
        endCell: CellAddress,
    ) {
        var onRow = true
        val targetCells:List<CellAddress> = if(startCell.colIndex != endCell.colIndex){
            onRow = false
            startCell.generateCellSequenceToCol(endCell.colIndex,false)
        }else{
            startCell.generateCellSequenceToRow(endCell.rowIndex,false)
        }

        val updateEntries = targetCells.map{
            val num:Double = startNum + if (onRow) it.rowIndex - startCell.rowIndex else it.colIndex - startCell.colIndex
            IndependentCellDM(
                address = it,
                content = CellContentDM.fromAny(num)
            )
        }

        val updateRequest = UpdateMultiCellRequest(
            wbKeySt = wbws.wbKeySt,
            wsNameSt = wbws.wsNameSt,
            cellUpdateList = updateEntries
        )
        multiCellUpdateAct.updateMultiCell(updateRequest,true)
    }

    /**
     * Copy content of [startCell] to all the cell within the range from [startCell] to [endCell].
     */
    private fun copyContent(wbws: WbWsSt, startCell: CellAddress, endCell: CellAddress) {
        val targetCells:List<CellAddress> = if(startCell.colIndex != endCell.colIndex){
            startCell.generateCellSequenceToCol(endCell.colIndex,false)
        }else{
            startCell.generateCellSequenceToRow(endCell.rowIndex,false)
        }
        val fromCell = CellIdDM(
            address = startCell,
            wbKey = wbws.wbKey,
            wsName = wbws.wsName
        )
        for(cell in targetCells){
            copyCellAct.copyCellWithoutClipboard(
                CopyCellRequest(
                    fromCell = fromCell,
                    toCell =CellIdDM(
                        address = cell,
                        wbKey = wbws.wbKey,
                        wsName = wbws.wsName
                    )
                ),false
            )
        }
    }
}
