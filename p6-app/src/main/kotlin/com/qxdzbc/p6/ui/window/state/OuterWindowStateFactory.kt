package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.common.compose.Ms
import dagger.assisted.AssistedFactory

@AssistedFactory
interface OuterWindowStateFactory{
    fun create(innerWindowStateMs: WindowState): OuterWindowStateImp
}
