package com.qxdzbc.p6.ui.window.move_to_wb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusState
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.ActiveWindowPointer

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class MoveToWbActionImp @Inject constructor(
    private val restoreWindowFocusState: RestoreWindowFocusState,
    private val stateContSt:St<@JvmSuppressWildcards StateContainer>,
    private val activeWindowPointerMs:Ms<ActiveWindowPointer>,
) : MoveToWbAction {

    private val sc by stateContSt
    private var activeWindowPointer by activeWindowPointerMs

    override fun moveToWorkbook(wbKey: WorkbookKey) {
        restoreWindowFocusState.setFocusStateConsideringRangeSelector(wbKey)
        var ces by sc.cellEditorStateMs
        if(ces.isOpen && ces.allowRangeSelector){
            val newRangeCursorMs = sc.getActiveCursorMs(wbKey)
            if(newRangeCursorMs!=null){
                ces = ces.setRangeSelectorCursorId(newRangeCursorMs.value.idMs)
            }else{
                // x: this happens when the target workbook is empty
                ces = ces.clearAllText().close()
                restoreWindowFocusState.restoreAllWsFocusIfRangeSelectorIsNotActive()
            }
        }else{
            ces = ces.close()
        }

        setActiveWb(wbKey)
    }

    override fun setActiveWb(wbKey: WorkbookKey) {
        val windowState = sc.getWindowStateByWbKey(wbKey)
        windowState?.also {wds->
            activeWindowPointerMs.value = activeWindowPointerMs.value.pointTo(wds.id)
            val wbkMs = sc.getWbKeyMs(wbKey)
            wbkMs?.also {
                wds.activeWbPointer = wds.activeWbPointer.pointTo(it)
            }
        }
    }
}
