package com.emeraldblast.p6.ui.window.state

import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import com.emeraldblast.p6.ui.window.formula_bar.FormulaBarState
import com.emeraldblast.p6.ui.window.formula_bar.FormulaBarStateImp
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess

abstract class BaseWindowState : WindowState{
    override val openCommonFileDialog:Boolean get()=this.commonFileDialogJob!=null
    override val size: Int get() = workbookStateMsList.size
    override val workbookList: List<Workbook> get() = wbKeySet.mapNotNull {
        this.globalWbContMs.value.getWb(it)
    }
    override fun publishError(errorReport: ErrorReport) :WindowState{
        this.oddityContainer = this.oddityContainer.addErrorReport(errorReport)
        return this
    }

    override val workbookStateList: List<WorkbookState>
        get() = this.workbookStateMsList.map { it.value }

//    override fun getWorkbookStateMsRs(workbookKey: WorkbookKey, onOk: (Ms<WorkbookState>) -> Unit) {
//        val rs = this.getWorkbookStateMsRs(workbookKey)
//        rs.onSuccess {
//            onOk(it)
//        }.onFailure {
//            this.publishError(it)
//        }
//    }
}
