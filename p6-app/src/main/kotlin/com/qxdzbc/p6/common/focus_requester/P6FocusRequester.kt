package com.qxdzbc.p6.common.focus_requester

import androidx.compose.ui.focus.FocusRequester

interface P6FocusRequester {
    fun freeFocus():Boolean
    fun requestFocus()
    val focusRequester:FocusRequester

    companion object{
        fun FocusRequester.toP6FocusRequester(): P6FocusRequester {
            return P6FocusRequesterImp(this)
        }

        val fake = object : P6FocusRequester {
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
