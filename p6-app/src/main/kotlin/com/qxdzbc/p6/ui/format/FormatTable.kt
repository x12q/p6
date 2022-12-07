package com.qxdzbc.p6.ui.format

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute


interface FormatTable<T> {

    val floatAttrMap:Map<T,Ms<MarkedAttribute<T>>>

    fun getFloatMarkedAttr(v:T):Ms<MarkedAttribute<T>>?
    fun addFloatMarkedAttr(v:T, attr:Ms<MarkedAttribute<T>>):FormatTable<T>

    fun add(v:T):Pair<CellFormatTable,MarkedAttribute<T>>
    fun reduceCountOf(v:T):CellFormatTable
    fun increaseCountOf(v:T):CellFormatTable

    fun removeFloatMarkedAttr(v:T):FormatTable<T>

    fun cleanUp():FormatTable<T>

}

