package com.emeraldblast.p6.ui.document.workbook.sheet_tab.bar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.document.wb_container.WorkbookContainer
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.emeraldblast.p6.ui.document.workbook.sheet_tab.tab.SheetTabState
import com.emeraldblast.p6.ui.document.workbook.sheet_tab.tab.SheetTabStateImp
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookId


data class SheetTabBarStateImp constructor(
    private val workbookIdMs: Ms<WorkbookId>,
    override val activeSheetPointerMs: Ms<ActiveWorksheetPointer>,
    private val wbContMs:Ms<WorkbookContainer>,
) : SheetTabBarState {
    private val wb get()= wbContMs.value.getWb(workbookIdMs.value.wbKey)
    override val tabStateList: List<SheetTabState> get() = wb?.worksheets?.map {
        SheetTabStateImp(
            sheetName = it.name,
            isSelected = activeSheetPointer.isPointingTo(it.name)
        )
    } ?: emptyList()

    private val map get()= tabStateList.associateBy { it.sheetName }
    override var activeSheetPointer: ActiveWorksheetPointer by this.activeSheetPointerMs

    override fun getTabState(sheetName: String): SheetTabState? {
        return map[sheetName]
    }

    override val size: Int
        get() = wb?.size ?: 0
}
