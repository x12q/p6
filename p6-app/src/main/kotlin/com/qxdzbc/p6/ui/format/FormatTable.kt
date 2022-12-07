package com.qxdzbc.p6.ui.format

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute


interface FormatTable<T> {

    val floatAttrMap:Map<T,Ms<MarkedAttribute<T>>>

    fun getMarkedAttr(v:T):Ms<MarkedAttribute<T>>?
    fun addMarkedAttr(v:T, attr:Ms<MarkedAttribute<T>>):FormatTable<T>

    fun add(v:T):Pair<FormatTable<T>,Ms<MarkedAttribute<T>>>

    fun reduceCountIfPossible(v:T):FormatTable<T>
    fun increaseCountIfPossible(v:T):FormatTable<T>
    fun changeCountIfPossible(v:T, count:Int):FormatTable<T>

    fun removeMarkedAttr(v:T):FormatTable<T>

    fun cleanUp():FormatTable<T>

}

