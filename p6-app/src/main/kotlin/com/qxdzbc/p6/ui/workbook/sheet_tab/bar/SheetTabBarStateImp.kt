package com.qxdzbc.p6.ui.workbook.sheet_tab.bar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.workbook.sheet_tab.tab.SheetTabState
import com.qxdzbc.p6.ui.workbook.sheet_tab.tab.SheetTabStateImp


data class SheetTabBarStateImp constructor(
    override val activeSheetPointerMs: Ms<ActiveWorksheetPointer>,
    private val wb:Workbook,
) : SheetTabBarState {
    override val tabStateList: List<SheetTabState> get() = wb.worksheets.map {
        SheetTabStateImp(
            sheetName = it.name,
            isSelected = activeSheetPointer.isPointingTo(it.name)
        )
    }

    private val map get()= tabStateList.associateBy { it.sheetName }
    override var activeSheetPointer: ActiveWorksheetPointer by this.activeSheetPointerMs

    override fun getTabState(sheetName: String): SheetTabState? {
        return map[sheetName]
    }

    override val size: Int
        get() = wb.size
}
