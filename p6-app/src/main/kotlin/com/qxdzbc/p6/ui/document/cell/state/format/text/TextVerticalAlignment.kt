package com.qxdzbc.p6.ui.document.cell.state.format.text

enum class TextVerticalAlignment {
    Top, Bot, Center;
    companion object {
        fun random():TextVerticalAlignment{
            return listOf(Top,Bot,Center).random()
        }
    }
}
