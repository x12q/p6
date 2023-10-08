package com.qxdzbc.p6.ui.example

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.window.singleWindowApplication
import com.qxdzbc.common.compose.StateUtils.rms

@OptIn(ExperimentalComposeUiApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)

data class ViewState(val isShow:Boolean, val fc:FocusRequester = FocusRequester()){
    fun rf(): ViewState {
        fc.requestFocus()
        return this
    }
    fun setShow(v:Boolean): ViewState {
        return this.copy(isShow=v)
    }
}

fun main() {
    singleWindowApplication(title = "Focus requester eg") {

        var state by rms(ViewState(false))

        Column{
            Button(onClick={
                state = state.setShow(true).rf()
            }) {
                Text("Focus view 1")
            }

            if(state.isShow){
                Text("View 1", modifier = Modifier.focusRequester(state.fc).focusable())
            }
        }
    }
}
