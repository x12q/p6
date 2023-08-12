package com.qxdzbc.p6.composite_actions.tool_bar.return_focus_to_cell

import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class ReturnFocusToCellCursorImp @Inject constructor(
    val stateContainerSt:StateContainer
) : ReturnFocusToCellCursor {

    val sc  = stateContainerSt

    override fun returnFocusToCurrentCellCursor() {
        sc.getActiveWindowState()?.focusStateMs?.also {
            it.value = it.value.focusOnCursor()
        }
    }
}
