package com.qxdzbc.p6.app.common.formatter

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import java.nio.file.Path

/**
 * Put range address string elements (range address, ws name, wb key) into correct string format that can be used inside a formula.
 * Eg: C1:C3@Sheet1@Wb3
 */
interface RangeAddressFormatter {
    fun format(cell:CellAddress,wsName:String?=null,wbKey:WorkbookKey?=null):String
    fun format(range:RangeAddress,wsName:String?=null,wbKey:WorkbookKey?=null):String
    fun format(rangeStr:String,wsName:String?=null,wbKey:WorkbookKey?=null):String
    fun formatStr(rangeStr:String, wsName:String?, wbKey:String?, path: Path?):String
}
