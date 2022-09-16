package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.build.BuildConfig
import com.qxdzbc.p6.app.build.BuildVariant
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState

abstract class BaseWindowState : WindowState {
    override val windowTitle: String
        get() {
            val l = mutableListOf("P6")
            val wbk = this.activeWbKey
            wbk?.also {
                l.add(wbk.name)
            }
            if(BuildConfig.currentFlavor == BuildVariant.DEBUG){
                l.add("wdId[${this.id}]")
            }
            return l.joinToString(" - ")
        }
    override val openCommonFileDialog: Boolean get() = this.commonFileDialogJob != null
    override val size: Int get() = wbStateMsList.size
    override val wbList: List<Workbook>
        get() = wbKeySet.mapNotNull {
            this.wbContMs.value.getWb(it)
        }

    override fun publishError(errorReport: ErrorReport): WindowState {
        this.oddityContainer = this.oddityContainer.addErrorReport(errorReport)
        return this
    }

    override val wbStateList: List<WorkbookState>
        get() = this.wbStateMsList.map { it.value }

//    override fun getWorkbookStateMsRs(workbookKey: WorkbookKey, onOk: (Ms<WorkbookState>) -> Unit) {
//        val rs = this.getWorkbookStateMsRs(workbookKey)
//        rs.onSuccess {
//            onOk(it)
//        }.onFailure {
//            this.publishError(it)
//        }
//    }
}
