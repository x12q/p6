package com.qxdzbc.p6.app_context

import com.qxdzbc.p6.di.P6Component
import com.qxdzbc.p6.ui.app.state.AppState

internal class GlobalAccessPointImp(
    private var p6Comp: P6Component? = null,
) : GlobalAccessPoint {

    override val appState: AppState get() = p6Comp?.appState()!!

    override fun setP6Component(p6Component: P6Component) {
        /*
        Only allow to set this once
         */
        if(p6Comp==null){
            this.p6Comp = p6Component
        }
    }

    override val p6Component: P6Component
        get() {
            return this.p6Comp!!
        }

}