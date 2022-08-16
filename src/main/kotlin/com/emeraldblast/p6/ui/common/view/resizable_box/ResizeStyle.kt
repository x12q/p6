package com.emeraldblast.p6.ui.common.view.resizable_box

import java.util.*


enum class ResizeStyle {
    __BOT, __TOP, __LEFT, __RIGHT;

    companion object {
        val ALL: EnumSet<ResizeStyle> = EnumSet.of(__BOT, __TOP, __LEFT, __RIGHT)
        val BOT_RIGHT = EnumSet.of(__BOT, __RIGHT)
        val TOP_BOT = EnumSet.of(__TOP, __BOT)
        val TOP_LEFT = EnumSet.of(__TOP, __LEFT)
        val TOP = EnumSet.of(__TOP)
        val BOT = EnumSet.of(__BOT)
        val RIGHT = EnumSet.of(__RIGHT)
        val LEFT = EnumSet.of(__LEFT)
        val LEFT_RIGHT = EnumSet.of(__LEFT, __RIGHT)

//        fun EnumSet<ResizeStyle>.debugAll(): EnumSet<ResizeStyle> {
//            return debug(ALL)
//        }
//        fun EnumSet<ResizeStyle>.debug(debugValue: EnumSet<ResizeStyle>): EnumSet<ResizeStyle> {
//            return if(BuildConfig.currentFlavor == BuildVariant.DEBUG){
//                debugValue
//            }else{
//                NONE
//            }
//        }
    }
}
