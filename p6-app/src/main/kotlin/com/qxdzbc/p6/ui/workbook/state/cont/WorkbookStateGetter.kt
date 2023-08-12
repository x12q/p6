package com.qxdzbc.p6.ui.workbook.state.cont

import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.state.StateContainerImp
import com.qxdzbc.p6.ui.workbook.state.WorkbookState

/**
 * An interface defining functions to get workbook state from the app's central state.
 * See [WorkbookStateContainer] and [StateContainerImp] for implementation
 */
interface WorkbookStateGetter{
    fun getWbStateRs(wbKey: WorkbookKey): Rse<WorkbookState>
    fun getWbState(wbKey: WorkbookKey): WorkbookState?
    fun getWbStateRs(wbKeySt: St<WorkbookKey>): Rse<WorkbookState>
    fun getWbState(wbKeySt: St<WorkbookKey>): WorkbookState?
}