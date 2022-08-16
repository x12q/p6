package com.emeraldblast.p6.ui.document.workbook.active_sheet_pointer

import com.emeraldblast.p6.app.document.worksheet.Worksheet
import com.emeraldblast.p6.ui.common.compose.St


interface ActiveWorksheetPointer {
    val wsNameSt:St<String>?
    val wsName:String?
    fun isValid():Boolean{
        return wsName!=null
    }
    fun isPointingTo(name:String): Boolean
    fun invalidate(): ActiveWorksheetPointer
    fun pointTo(anotherWorksheetName:St<String>?): ActiveWorksheetPointer
    fun pointTo(anotherWorksheet:Worksheet): ActiveWorksheetPointer
}
