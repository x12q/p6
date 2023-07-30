package com.qxdzbc.p6.app.app_context

import com.qxdzbc.p6.di.P6Component
import com.qxdzbc.p6.ui.app.state.AppState

/**
 * A global access point, this actually should not be used at all in any place other than the init code.
 */
interface GlobalAccessPoint {
    fun setP6Component(p6Component: P6Component)
    val p6Component: P6Component
    val appState:AppState
    fun setAppState(i:AppState)
}

val P6GlobalAccessPoint:GlobalAccessPoint = GlobalAccessPointImp()
