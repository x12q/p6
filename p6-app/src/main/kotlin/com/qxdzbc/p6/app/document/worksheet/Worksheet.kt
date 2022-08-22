package com.qxdzbc.p6.app.document.worksheet

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.common.utils.WithSize
import com.qxdzbc.p6.app.common.table.TableCR
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.cell.d.CellContent
import com.qxdzbc.p6.app.document.cell.d.CellImp
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerErrors
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.proto.DocProtos.WorksheetProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.common.compose.St
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraint
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

interface Worksheet : WithSize,WbWsSt {

    fun reRun():Worksheet

    val idMs:Ms<WorksheetId>
    var id:WorksheetId

    val nameMs:Ms<String>
    val name:String

    fun setWbKeySt(wbKeySt:St<WorkbookKey>):Worksheet



    val table: TableCR<Int, Int, Ms<Cell>>
    val cells:List<Cell> get()=cellMsList.map{it.value}
    val cellMsList:List<Ms<Cell>>
    val rangeConstraint:RangeConstraint
    override val size:Int get() = table.itemCount

    fun toProto():WorksheetProto

    /**
     * return a range derived from this worksheet
     */
    fun range(address: RangeAddress): Result<Range,ErrorReport>

    fun updateCellValue(cellAddress: CellAddress,value:Any?): Result<Worksheet, ErrorReport>
    fun updateCellContentRs(cellAddress: CellAddress, cellContent: CellContent):Result<Worksheet, ErrorReport>

    fun getCellsInRange(rangeAddress: RangeAddress):List<Cell>{
        return this.getCellMsInRange(rangeAddress).map{it.value}
    }
    fun getCellMsInRange(rangeAddress: RangeAddress):List<Ms<Cell>>

    fun getCellMsOrNull(cellAddress: CellAddress): Ms<Cell>?{
        return table.getElement(cellAddress.colIndex,cellAddress.rowIndex)
    }
    fun getCellMsOrNull(colIndex: Int, rowIndex: Int): Ms<Cell>?{
        return table.getElement(colIndex,rowIndex)
    }
    fun getCellMsOrNull(label:String): Ms<Cell>?{
        return table.getElement(CellAddress(label))
    }

    fun getCellOrNull(cellAddress: CellAddress): Cell?{
        return getCellMsOrNull(cellAddress)?.value
    }
    fun getCellOrNull(colIndex: Int, rowIndex: Int): Cell?{
        return getCellMsOrNull(colIndex,rowIndex)?.value
    }
    fun getCellOrNull(label:String): Cell?{
        return getCellMsOrNull(CellAddress(label))?.value
    }


    fun getCellOrDefaultRs(cellAddress: CellAddress): Result<Cell, ErrorReport> {
        if(rangeConstraint.contains(cellAddress)){
            return Ok(getCellOrNull(cellAddress) ?: CellImp(cellAddress))
        }else{
            WorkbookContainerErrors
            return Err(WorksheetErrors.InvalidCell(cellAddress))
        }
    }

    fun addOrOverwrite(cell: Cell): Worksheet

    fun getColMs(colIndex:Int):List<Ms<Cell>>{
        return table.getCol(colIndex)
    }

    fun getRowMs(rowIndex:Int):List<Ms<Cell>>{
        return table.getRow(rowIndex)
    }

    fun getCol(colIndex:Int):List<Cell>{
        return getColMs(colIndex).map{it.value}
    }

    fun getRow(rowIndex:Int):List<Cell>{
        return getRowMs(rowIndex).map { it.value }
    }

    fun removeCol(colIndex: Int): Worksheet
    fun removeRow(rowIndex: Int): Worksheet
    fun removeCell(colKey: Int, rowKey: Int): Worksheet
    fun removeCell(cellAddress: CellAddress): Worksheet {
        return this.removeCell(cellAddress.colIndex, cellAddress.rowIndex)
    }
    fun removeCells(cells:Collection<CellAddress>):Worksheet
    fun setWsName(newName:String, translator: P6Translator<ExUnit>): Worksheet
    fun withNewData(wsProto: WorksheetProto, translator: P6Translator<ExUnit>): Worksheet
}
