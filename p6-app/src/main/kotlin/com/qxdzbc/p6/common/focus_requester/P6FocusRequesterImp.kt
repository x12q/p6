package com.qxdzbc.p6.common.focus_requester

import androidx.compose.ui.focus.FocusRequester

class P6FocusRequesterImp (override val focusRequester: FocusRequester) :
    P6FocusRequester {
    override fun freeFocus(): Boolean {
        return focusRequester.freeFocus()
    }

    override fun requestFocus() {
        focusRequester.requestFocus()
    }
}
