package com.qxdzbc.p6.ui.format

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.format.attr.BoolAttr
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(scope = P6AnvilScope::class)
data class CellFormatTableImp @Inject constructor(
    override val floatTable: FormatTable<Float>,
    override val colorTable: FormatTable<Color>,
    override val boolTable: FormatTable<BoolAttr>,
    override val horizontalAlignmentTable: FormatTable<TextHorizontalAlignment>
) : CellFormatTable {

    constructor() : this(FormatTableImp(), FormatTableImp(),FormatTableImp(),FormatTableImp())

    override fun updateFloatTable(i: FormatTable<Float>): CellFormatTableImp {
        return this.copy(floatTable = i)
    }

    override fun updateColorTable(i: FormatTable<Color>): CellFormatTableImp {
        return this.copy(colorTable = i)
    }

    override fun updateBoolTable(i: FormatTable<BoolAttr>): CellFormatTableImp {
        return this.copy(boolTable = i)
    }

    override fun updateHorizontalAlignmentTable(i: FormatTable<TextHorizontalAlignment>): CellFormatTable {
        return this.copy(horizontalAlignmentTable = i)
    }
}
