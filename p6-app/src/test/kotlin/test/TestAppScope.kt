package test

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import test.di.TestComponent

interface TestAppScope{
    val ts: TestSample
    val appState: AppState
    val sc: StateContainer
    val comp: TestComponent
}
