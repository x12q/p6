package com.emeraldblast.p6.ui.document.workbook.active_sheet_pointer

import com.emeraldblast.p6.app.document.worksheet.Worksheet
import com.emeraldblast.p6.ui.common.compose.St

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
