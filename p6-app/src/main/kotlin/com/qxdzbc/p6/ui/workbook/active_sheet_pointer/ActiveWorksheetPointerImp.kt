package com.qxdzbc.p6.ui.workbook.active_sheet_pointer

import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.common.compose.St

data class ActiveWorksheetPointerImp(override val wsNameSt: St<String>? = null) : ActiveWorksheetPointer {

    override val wsName: String? get()=wsNameSt?.value
    override fun isPointingTo(name: String):Boolean {
        return this.wsName == name
    }

    override fun invalidate(): ActiveWorksheetPointer {
        return this.copy(null)
    }

    override fun pointTo(anotherWorksheetName: St<String>?): ActiveWorksheetPointer {
        if(anotherWorksheetName == this.wsNameSt){
            return this
        }else{
            return this.copy(wsNameSt = anotherWorksheetName)
        }
    }

    override fun pointTo(anotherWorksheet: Worksheet): ActiveWorksheetPointer {
        return this.pointTo(anotherWorksheet.nameMs)
    }

}
