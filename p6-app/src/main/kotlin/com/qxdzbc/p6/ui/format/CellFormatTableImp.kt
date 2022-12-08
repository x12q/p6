package com.qxdzbc.p6.ui.format

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(scope = P6AnvilScope::class)
data class CellFormatTableImp @Inject constructor(
    override val floatTable: FormatTable<Float>,
    override val colorTable: FormatTable<Color>
) : CellFormatTable {
    constructor() : this(FormatTableImp(), FormatTableImp())

    override fun updateFloatTable(i: FormatTable<Float>): CellFormatTableImp {
        return this.copy(floatTable = i)
    }

    override fun updateColorTable(i: FormatTable<Color>): CellFormatTable {
        return this.copy(colorTable = i)
    }
}


