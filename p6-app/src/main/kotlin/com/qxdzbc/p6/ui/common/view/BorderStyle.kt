package com.qxdzbc.p6.ui.common.view

import com.qxdzbc.p6.build.BuildConfig
import com.qxdzbc.p6.build.BuildVariant
import java.util.*

sealed class BorderStyle(
    val borderStyleValueSet: EnumSet<BorderStyleValue>
) {
    object ALL:BorderStyle(BorderStyleValue.ALL)
    object BOT_RIGHT :BorderStyle(BorderStyleValue.BOT_RIGHT)
    object TOP_BOT:BorderStyle(BorderStyleValue.TOP_BOT)
    object TOP_LEFT:BorderStyle(BorderStyleValue.TOP_LEFT)
    object TOP: BorderStyle(BorderStyleValue.TOP)
    object BOT:BorderStyle(BorderStyleValue.BOT)
    object RIGHT:BorderStyle(BorderStyleValue.RIGHT)
    object LEFT:BorderStyle(BorderStyleValue.LEFT)
    object LEFT_RIGHT:BorderStyle(BorderStyleValue.LEFT_RIGHT)
    object NONE:BorderStyle(BorderStyleValue.NONE)

    operator fun contains(bs: BorderStyleValue):Boolean{
        return bs in this.borderStyleValueSet
    }

    fun debugAll(): BorderStyle {
        return debug(ALL)
    }

    fun debug(debugValue: BorderStyle): BorderStyle {
        return if (BuildConfig.buildVariant == BuildVariant.DEBUG) {
            debugValue
        } else {
            BorderStyle.NONE
        }
    }
}