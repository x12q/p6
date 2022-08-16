package com.emeraldblast.p6.ui.window.state

import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey


interface ActiveWorkbookPointer {
    val wbKey:WorkbookKey?
    fun isValid():Boolean{
        return wbKey!=null
    }
    fun isPointingTo(workbook: Workbook): Boolean{
        return this.isPointingTo(workbook.key)
    }
    fun isPointingTo(workbookKey: WorkbookKey): Boolean

    fun nullify(): ActiveWorkbookPointer

    fun pointTo(workbookKey: WorkbookKey?): ActiveWorkbookPointer
    fun pointTo(workbook:Workbook): ActiveWorkbookPointer {
        return this.pointTo(workbook.key)
    }
}
