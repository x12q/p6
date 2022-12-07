package com.qxdzbc.p6.ui.format

import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(scope=P6AnvilScope::class)
data class CellFormatTableImp @Inject constructor(override val floatValueFormatTable: FormatTable<Float>) : CellFormatTable{
    constructor():this(FormatTableImp())

    override fun updateFloatFormatTable(i: FormatTable<Float>): CellFormatTableImp {
        return this.copy(floatValueFormatTable = i)
    }
}


