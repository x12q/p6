package com.qxdzbc.p6.ui.document.cell.state.format.text

enum class TextHorizontalAlignment {
    Start, End, Center;
    companion object{
        fun random():TextHorizontalAlignment{
            return listOf(Start, End, Center).random()
        }
    }
}
