package com.qxdzbc.p6.ui.window.workbook_tab.tab

import com.qxdzbc.common.RandomUtils
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

data class WorkbookTabStateImp(
    override val wbKey: WorkbookKey,
    override val isSelected: Boolean,
    override val needSave: Boolean,
) : WorkbookTabState {
    override val tabName: String
        get() = if (needSave) {
            "${wbKey.name}*"
        } else {
            wbKey.name
        }

    companion object{
        fun random():WorkbookTabStateImp{
            return WorkbookTabStateImp(
                wbKey = WorkbookKey.random(),
                isSelected = RandomUtils.randomBool(),
                needSave = RandomUtils.randomBool(),
            )
        }
    }
}
