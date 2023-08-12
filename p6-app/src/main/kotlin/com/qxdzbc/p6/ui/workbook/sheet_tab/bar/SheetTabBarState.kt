package com.qxdzbc.p6.ui.workbook.sheet_tab.bar

import com.qxdzbc.common.WithSize
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.workbook.sheet_tab.tab.SheetTabState

interface SheetTabBarState : WithSize {
    val activeSheetPointerMs:Ms<ActiveWorksheetPointer>
    var activeSheetPointer:ActiveWorksheetPointer
    val tabStateList: List<SheetTabState>
    fun getTabState(sheetName: String): SheetTabState?
}
