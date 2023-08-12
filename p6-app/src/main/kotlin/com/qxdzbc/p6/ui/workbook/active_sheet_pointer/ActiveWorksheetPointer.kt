package com.qxdzbc.p6.ui.workbook.active_sheet_pointer

import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.common.compose.St


/**
 * A class for indicating which worksheet is currently selected and shown on the screen.
 */
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
