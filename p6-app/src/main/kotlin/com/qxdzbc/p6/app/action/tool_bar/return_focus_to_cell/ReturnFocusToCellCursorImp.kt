package com.qxdzbc.p6.app.action.tool_bar.return_focus_to_cell

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class ReturnFocusToCellCursorImp @Inject constructor(
    val stateContainerSt:St<@JvmSuppressWildcards StateContainer>
) : ReturnFocusToCellCursor {

    val sc by stateContainerSt

    override fun returnFocusToCurrentCellCursor() {
        sc.getActiveWindowState()?.focusStateMs?.also {
            it.value = it.value.focusOnCursor()
        }
    }
}
