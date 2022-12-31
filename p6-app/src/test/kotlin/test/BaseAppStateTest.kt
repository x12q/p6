package test

import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import kotlin.test.BeforeTest
import com.qxdzbc.common.test_util.TestSplitter

/**
 * A base test class that provided a ready-to-use app state. This app state is identical to the one used in the actual app.
 */
abstract class BaseAppStateTest : TestSplitter() {
    private var _ts: TestSample? = null
    val ts: TestSample
        get() {

            if (_ts != null) {
                return _ts!!
            } else {
                throw NullPointerException("ts is not init yet")
            }
        }
    val appState: AppState get() = ts.appState
    val sc: StateContainer get() = ts.stateCont
    val comp get() = ts.comp

    @BeforeTest
    open fun _b() {
        _ts = TestSample()
    }
}

