package test

import com.qxdzbc.p6.di.P6Component
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import test.TestSample

interface TestAppScope{
    val ts: TestSample
    val appState: AppState
    val sc: StateContainer
    val p6Comp: TestComponent
}
