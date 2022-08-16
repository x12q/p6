package com.emeraldblast.p6.ui.action_table

import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.app.action.workbook.WorkbookAction
import javax.inject.Inject

class WorkbookActionTableImp @Inject constructor(
    @AppStateMs private val appStateMs:Ms<AppState>,
    private val wbAction: WorkbookAction,
    override val worksheetActionTable: WorksheetActionTable
) : WorkbookActionTable {

    val appState get()=appStateMs.value

    override fun getWbAction(): WorkbookAction {
        return wbAction
    }
}
