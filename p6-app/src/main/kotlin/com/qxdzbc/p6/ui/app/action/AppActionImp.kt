package com.qxdzbc.p6.ui.app.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ApplicationScope
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.common.compose.Ms
import javax.inject.Inject

class AppActionImp @Inject constructor(
    private val appScope: ApplicationScope?,
    @AppStateMs
    private val appStateMs: Ms<AppState>,
) : AppAction {

    private var appState by appStateMs

    override fun exitApp() {
        appScope?.exitApplication()
    }

    override fun closeCodeEditor() {
        appState = appState.closeCodeEditor()
    }

    override fun openCodeEditor() {
        appState = appState.openCodeEditor()
    }
}