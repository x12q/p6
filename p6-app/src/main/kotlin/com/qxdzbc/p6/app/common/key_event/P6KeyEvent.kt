package com.qxdzbc.p6.app.common.key_event

import androidx.compose.ui.input.key.KeyEvent
import com.qxdzbc.common.compose.key_event.MKeyEvent

interface P6KeyEvent : MKeyEvent {
    companion object{
        fun KeyEvent.toP6KeyEvent():P6KeyEvent{
            return P6KeyEventWrapper(this)
        }
    }
    /**
     * is a key accepted by a range selector
     */
    fun isAcceptedByRangeSelector():Boolean

    /**
     * is a key a navigation key accepted by a range selector. Including:
     *  - arrow keys
     *  - ctrl + shift + arrow keys
     *  - ctrl + arrow keys
     *  - shift + arrow keys
     *  - home key
     *  - end key
     */
    fun isRangeSelectorNavKey():Boolean

    /**
     * is a key a non-navigation key accepted by a range selector. Including:
     *  - ctrl
     *  - shift
     *  - alt
     */
    fun isRangeSelectorNonNavKey():Boolean
}
