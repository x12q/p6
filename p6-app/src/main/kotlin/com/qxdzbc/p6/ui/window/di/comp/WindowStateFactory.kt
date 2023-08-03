package com.qxdzbc.p6.ui.window.di.comp

import com.qxdzbc.p6.ui.window.state.WindowState
import java.util.*

interface WindowStateFactory {
    fun createDefault(
        id: String = UUID.randomUUID().toString()
    ): WindowState
}
