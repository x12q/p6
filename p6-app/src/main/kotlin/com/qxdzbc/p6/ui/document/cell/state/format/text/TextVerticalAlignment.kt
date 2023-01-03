package com.qxdzbc.p6.ui.document.cell.state.format.text

import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toTextVerticalAlignmentModel

enum class TextVerticalAlignment {
    Top, Bot, Center;
    companion object {
        fun random():TextVerticalAlignment{
            return listOf(Top,Bot,Center).random()
        }
        fun fromInt(i:Int,default:TextVerticalAlignment=Center):TextVerticalAlignment{
            return when (i) {
                Top.ordinal -> Top
                Bot.ordinal -> Bot
                Center.ordinal -> Center
                else -> default
            }
        }
        fun fromIntOrNull(i:Int):TextVerticalAlignment?{
            return when (i) {
                Top.ordinal -> Top
                Bot.ordinal -> Bot
                Center.ordinal -> Center
                else -> null
            }
        }
    }
}
