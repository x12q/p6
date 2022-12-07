package com.qxdzbc.p6.ui.format

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute

data class FormatTableImp<T>(
    override val floatAttrMap: Map<T, Ms<MarkedAttribute<T>>> = emptyMap()
) : FormatTable<T> {
    override fun getFloatMarkedAttr(v: T): Ms<MarkedAttribute<T>>? {
        return floatAttrMap[v]
    }

    override fun addFloatMarkedAttr(v: T, attr: Ms<MarkedAttribute<T>>): FormatTableImp<T> {
        return this.copy(
            floatAttrMap = floatAttrMap + (v to attr)
        )
    }


    override fun removeFloatMarkedAttr(v: T): FormatTableImp<T> {
        return this.copy(
            floatAttrMap = floatAttrMap - v
        )
    }

    override fun add(v: T): Pair<CellFormatTable, MarkedAttribute<T>> {
        TODO("Not yet implemented")
    }

    override fun reduceCountOf(v: T): CellFormatTable {
        TODO("Not yet implemented")
    }

    override fun increaseCountOf(v: T): CellFormatTable {
        TODO("Not yet implemented")
    }

    override fun cleanUp(): FormatTableImp<T> {
        return this.copy(
            floatAttrMap = floatAttrMap.filter { it.value.value.isCounterNotZero }
        )
    }
}
