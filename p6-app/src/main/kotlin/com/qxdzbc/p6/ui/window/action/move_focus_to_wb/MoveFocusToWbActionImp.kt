package com.qxdzbc.p6.ui.window.action.move_focus_to_wb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.composite_actions.worksheet.release_focus.RestoreWindowFocusState
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.ActiveWindowPointer

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class MoveFocusToWbActionImp @Inject constructor(
    private val restoreWindowFocusState: RestoreWindowFocusState,
    private val sc: StateContainer,
    private val activeWindowPointer:ActiveWindowPointer,
) : MoveFocusToWbAction {

    override fun moveToWorkbook(wbKey: WorkbookKey) {
        restoreWindowFocusState.setFocusStateConsideringRangeSelector(wbKey)
        var ces by sc.cellEditorStateMs
        if(ces.isOpen && ces.allowRangeSelector){
            val newRangeCursorMs = sc.getActiveCursorMs(wbKey)
            if(newRangeCursorMs!=null){
                ces = ces.setRangeSelectorId(newRangeCursorMs.value.idMs)
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
            activeWindowPointer.pointTo(wds.id)
            val wbkMs = sc.getWbKeyMs(wbKey)
            wbkMs?.also {
                wds.activeWbPointerMs.value = wds.activeWbPointer.pointTo(it)
            }
        }
    }
}
