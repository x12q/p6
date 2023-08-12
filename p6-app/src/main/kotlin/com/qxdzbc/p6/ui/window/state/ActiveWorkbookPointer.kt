package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey


interface ActiveWorkbookPointer {
    val wbKeyMs:Ms<WorkbookKey>?
    val wbKey:WorkbookKey?
    fun isValid():Boolean{
        return wbKey!=null
    }
    fun isPointingTo(workbook: Workbook): Boolean{
        return this.isPointingTo(workbook.key)
    }
    fun isPointingTo(workbookKey: WorkbookKey): Boolean
    fun isPointingTo(wbKeyMs:St<WorkbookKey> ): Boolean

    fun nullify(): ActiveWorkbookPointer

    fun pointTo(wbKeyMs: Ms<WorkbookKey>?): ActiveWorkbookPointer
    fun pointTo(workbook:Workbook): ActiveWorkbookPointer {
        return this.pointTo(workbook.keyMs)
    }
}
