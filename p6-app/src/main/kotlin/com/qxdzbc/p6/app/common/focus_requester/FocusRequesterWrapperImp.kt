package com.qxdzbc.p6.app.common.focus_requester

import androidx.compose.ui.focus.FocusRequester

class FocusRequesterWrapperImp (override val focusRequester: FocusRequester) : FocusRequesterWrapper {
    override fun freeFocus(): Boolean {
        return focusRequester.freeFocus()
    }

    override fun requestFocus() {
        focusRequester.requestFocus()
    }
}
