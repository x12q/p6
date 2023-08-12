package com.qxdzbc.p6.ui.cell.state.format.text

enum class TextHorizontalAlignment {
    Start, End, Center;
    companion object{
        fun random(): TextHorizontalAlignment {
            return listOf(Start, End, Center).random()
        }
        fun fromInt(i:Int,default: TextHorizontalAlignment = Start): TextHorizontalAlignment {
            return when(i){
                Start.ordinal-> Start
                End.ordinal-> End
                Center.ordinal-> Center
                else -> default
            }
        }

        fun fromIntOrNull(i:Int): TextHorizontalAlignment?{
            return when(i){
                Start.ordinal-> Start
                End.ordinal-> End
                Center.ordinal-> Center
                else -> null
            }
        }
    }
}
