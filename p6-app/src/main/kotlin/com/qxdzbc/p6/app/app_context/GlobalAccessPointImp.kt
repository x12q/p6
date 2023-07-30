package com.qxdzbc.p6.app.app_context

import com.qxdzbc.p6.di.P6Component
import com.qxdzbc.p6.ui.app.state.AppState

internal class GlobalAccessPointImp(
    private var p6Comp: P6Component? = null,
    var _appState: AppState? = null,
) : GlobalAccessPoint {

    override val appState: AppState get() = _appState!!
    override fun setAppState(i: AppState) {
        _appState = i
    }

    override fun setP6Component(p6Component: P6Component) {
        this.p6Comp = p6Component
    }

    override val p6Component: P6Component
        get() {
            return this.p6Comp!!
        }

}