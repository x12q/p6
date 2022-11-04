package com.qxdzbc.p6.app.common.formatter

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import java.nio.file.Path

interface RangeAddressFormatter {
    fun format(cell:CellAddress,wsName:String?=null,wbKey:WorkbookKey?=null):String
    fun format(range:RangeAddress,wsName:String?=null,wbKey:WorkbookKey?=null):String
    fun format(rangeStr:String,wsName:String?=null,wbKey:WorkbookKey?=null):String
    fun formatStr(rangeStr:String, wsName:String?, wbKey:String?, path: Path?):String
}
