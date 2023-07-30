package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.build.BuildConfig
import com.qxdzbc.p6.build.BuildVariant
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.window.tool_bar.state.ToolBarState

abstract class BaseWindowState : WindowState {

    override val toolBarState: ToolBarState
        get() = toolBarStateMs.value

    override val windowTitle: String
        get() {
            val l = mutableListOf("P6")
            val wbk = this.activeWbKey
            wbk?.also {
                l.add(wbk.name)
            }
            if(BuildConfig.buildVariant == BuildVariant.DEBUG){
                l.add("wdId[${this.id}]")
            }
            return l.joinToString(" - ")
        }
    override val openCommonFileDialog: Boolean get() = this.commonFileDialogJob != null

    override val size: Int get() = wbStateMsList.size

    override val wbStateList: List<WorkbookState>
        get() = this.wbStateMsList

}
