package com.qxdzbc.p6.app.common.focus_requester

import androidx.compose.ui.focus.FocusRequester

interface FocusRequesterWrapper {
    fun freeFocus():Boolean
    fun requestFocus()
    val focusRequester:FocusRequester

    companion object{
        fun FocusRequester.wrap():FocusRequesterWrapper{
            return FocusRequesterWrapperImp(this)
        }

        val fake = object : FocusRequesterWrapper{
            override fun freeFocus(): Boolean {
                return true
            }

            override fun requestFocus() {
                // do nothing
            }

            override val focusRequester: FocusRequester
                get() = TODO("not support")

        }
    }
}
