package com.qxdzbc.p6.app.app_context

import com.qxdzbc.p6.di.P6Component
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms

/**
 * A global access point, this actually should not be used at all in any place other than the init code.
 */
interface GlobalAccessPoint {
    fun setP6Component(p6Component: P6Component)
    val p6Component: P6Component

    val appStateMs:Ms<AppState>
    fun setAppStateMs(appStateMs:Ms<AppState>)
}

internal class GlobalAccessPointImp(private var p6Comp: P6Component? = null, private var _appStateMs: Ms<AppState>?=null) : GlobalAccessPoint {

    override fun setP6Component(p6Component: P6Component) {
        this.p6Comp = p6Component
    }

    override val p6Component: P6Component
        get() {
            return this.p6Comp!!
        }
    override val appStateMs: Ms<AppState>
        get() = this._appStateMs!!

    override fun setAppStateMs(appStateMs: Ms<AppState>) {
        this._appStateMs = appStateMs
    }
}

val P6GlobalAccessPoint:GlobalAccessPoint = GlobalAccessPointImp()
