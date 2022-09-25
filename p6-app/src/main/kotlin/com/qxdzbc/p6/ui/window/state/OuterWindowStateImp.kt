package com.qxdzbc.p6.ui.window.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

data class OuterWindowStateImp @AssistedInject constructor(
    @Assisted override val innerWindowStateMs: Ms<WindowState>
) : OuterWindowState {
    override var innerWindowState: WindowState by innerWindowStateMs

    override val id:String get()=innerWindowState.id
}
