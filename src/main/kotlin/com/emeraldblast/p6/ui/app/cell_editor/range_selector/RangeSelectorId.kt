package com.emeraldblast.p6.ui.app.cell_editor.range_selector

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St

interface RangeSelectorId {
    val windowId:String?
    fun setWindowId(windowId:String?): RangeSelectorId

    val wbKeySt:St<WorkbookKey>?
    val wbKey:WorkbookKey?
    fun setWbKeySt(wbKeySt:St<WorkbookKey>?): RangeSelectorId

    val wsNameSt:St<String>?
    val wsName:String?
    fun setWsNameSt(wsNameSt:Ms<String>?): RangeSelectorId
}
