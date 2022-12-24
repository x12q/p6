package com.qxdzbc.p6.ui.document.cell.state.format.cell

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

data class CellFormatImp(
    override val backgroundColor: Color? = null
) : CellFormat {

    override fun setBackgroundColor(i: Color?): CellFormatImp {
        return this.copy(backgroundColor = i)
    }

    override val boxModifier: Modifier
        get() = Modifier
            .background(backgroundColor ?: CellFormat.defaultBackgroundColor)

    companion object {
        val default = CellFormatImp()
    }
}
