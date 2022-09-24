package test

import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import kotlin.test.BeforeTest

abstract class BaseTest {
    lateinit var ts: TestSample
    val appState: AppState get() = ts.appState
    val sc: StateContainer get() = ts.stateCont
    val p6Comp get()=ts.p6Comp


    @BeforeTest
    fun b() {
        ts = TestSample()
    }

}
