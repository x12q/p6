package test.test_implementation

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import com.emeraldblast.p6.app.common.utils.AbstractPKeyEvent

class MockPKeyEvent(
    override val key: Key,
    override val type: KeyEventType = KeyEventType.KeyDown,
    override val isCtrlShiftPressed: Boolean = false,
    override val isShiftPressedAlone: Boolean = false,
    override val isCtrlPressedAlone: Boolean = false,
    private val isRangeSelectorToleratedKey: Boolean? = null,
    private val isRangeSelectorNavKey: Boolean? = null,
    private val isRangeSelectorNonNavKey: Boolean? = null,
) : AbstractPKeyEvent() {
    override val keyEvent: KeyEvent get() = throw UnsupportedOperationException("mock key event does support getting the real key event")

    override fun isRangeSelectorToleratedKey(): Boolean {
        return isRangeSelectorToleratedKey ?: super.isRangeSelectorToleratedKey()
    }

    override fun isRangeSelectorNavKey(): Boolean {
        return isRangeSelectorNavKey ?: super.isRangeSelectorNavKey()
    }

    override fun isRangeSelectorNonNavKey(): Boolean {
        return isRangeSelectorNonNavKey ?: super.isRangeSelectorNonNavKey()
    }
}
