package com.qxdzbc.p6.app.action.cursor.thumb.drag_thumb_action

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellRequest
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest
import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class EndThumbDragActionImp @Inject constructor(
    val stateContainerSt: St<@JvmSuppressWildcards StateContainer>,
    private val copyCellAct: CopyCellAction,
    private val multiCellUpdateAct: UpdateMultiCellAction,
) : EndThumbDragAction {

    val sc by stateContainerSt

    override fun invokeSuitableAction(wbws: WbWsSt, startCell: CellAddress, endCell: CellAddress, isCtrPressed: Boolean) {
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

    fun copyContent(wbws: WbWsSt, startCell: CellAddress, endCell: CellAddress) {
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
