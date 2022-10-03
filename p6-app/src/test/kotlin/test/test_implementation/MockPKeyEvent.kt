package test.test_implementation

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import com.qxdzbc.common.compose.key_event.AbsPKeyEvent

data class MockPKeyEvent @OptIn(ExperimentalComposeUiApi::class) constructor(
    override val key: Key = Key.Unknown,
    override val type: KeyEventType = KeyEventType.KeyDown,
    override val isCtrlShiftPressed: Boolean = false,
    override val isShiftPressedAlone: Boolean = false,
    override val isCtrlPressedAlone: Boolean = false,
    private val isAcceptedByRangeSelector: Boolean? = null,
    private val isRangeSelectorNavKey: Boolean? = null,
    private val isRangeSelectorNonNavKey: Boolean? = null,
    override val isAltPressedAlone: Boolean = false,
    override val isShiftPressedInCombination: Boolean = false,
    override val isFreeOfModificationKey: Boolean = true,
    val isParentheses: Boolean? = null,
    val isBracket: Boolean? = null,
    val isCurlyBracket: Boolean? = null,
) : AbsPKeyEvent() {

    companion object {
        @OptIn(ExperimentalComposeUiApi::class)
        val arrowDown = MockPKeyEvent(
            key = Key.DirectionDown,
            type = KeyEventType.KeyDown,
            isAcceptedByRangeSelector = true,
            isRangeSelectorNavKey = true,
        )
    }

    override val keyEvent: KeyEvent get() = throw UnsupportedOperationException("mock key event does support getting the real key event")

    override fun isAcceptedByRangeSelector(): Boolean {
        return isAcceptedByRangeSelector ?: super.isAcceptedByRangeSelector()
    }

    override fun isRangeSelectorNavKey(): Boolean {
        return isRangeSelectorNavKey ?: super.isRangeSelectorNavKey()
    }

    override fun isRangeSelectorNonNavKey(): Boolean {
        return isRangeSelectorNonNavKey ?: super.isRangeSelectorNonNavKey()
    }

    override val isLeftParentheses: Boolean
        get() = this.isParentheses ?: super.isLeftParentheses

    override val isLeftBracket: Boolean
        get() = this.isBracket ?: super.isLeftBracket

    override val isLeftCurlyBracket: Boolean
        get() = this.isCurlyBracket ?: super.isLeftCurlyBracket
}
