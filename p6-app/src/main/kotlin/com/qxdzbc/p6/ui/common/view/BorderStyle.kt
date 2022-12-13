package com.qxdzbc.p6.ui.common.view

import com.qxdzbc.p6.build.BuildConfig
import com.qxdzbc.p6.build.BuildVariant
import java.util.*

enum class BorderStyle {
    __BOT, __TOP, __LEFT, __RIGHT, __NONE;

    companion object {
        val ALL: EnumSet<BorderStyle> = EnumSet.of(__BOT, __TOP, __LEFT, __RIGHT)
        val BOT_RIGHT = EnumSet.of(__BOT, __RIGHT)
        val TOP_BOT = EnumSet.of(__TOP, __BOT)
        val TOP_LEFT = EnumSet.of(__TOP, __LEFT)
        val TOP = EnumSet.of(__TOP)
        val BOT = EnumSet.of(__BOT)
        val RIGHT = EnumSet.of(__RIGHT)
        val LEFT = EnumSet.of(__LEFT)
        val LEFT_RIGHT = EnumSet.of(__LEFT, __RIGHT)
        val NONE = EnumSet.of(__NONE)

        fun EnumSet<BorderStyle>.debugAll(): EnumSet<BorderStyle> {
            return debug(ALL)
        }
        fun EnumSet<BorderStyle>.debug(debugValue: EnumSet<BorderStyle>): EnumSet<BorderStyle> {
            return if(BuildConfig.buildVariant == BuildVariant.DEBUG){
                debugValue
            }else{
                NONE
            }
        }
    }
}
