package com.qxdzbc.p6.ui.format

import com.qxdzbc.p6.ui.format.marked.MarkedAttribute


interface FormatTable<T> {

    val attrMap:Map<T,MarkedAttribute<T>>

    fun getMarkedAttr(v:T):MarkedAttribute<T>?

    fun addMarkedAttr(v:T, attr:MarkedAttribute<T>):FormatTable<T>

    fun addAndGetMarkedAttr(v:T):Pair<FormatTable<T>,MarkedAttribute<T>>

    fun reduceCountIfPossible(v:T):FormatTable<T>
    fun increaseCountIfPossible(v:T):FormatTable<T>
    fun changeCountIfPossible(v:T, count:Int):FormatTable<T>

    fun removeMarkedAttr(v:T):FormatTable<T>

    fun removeAll():FormatTable<T>

    /**
     * Try to add a value [v] as a marked attribute, update the respective marked attribute already exist, otherwise, create a new marked attribute and add it to this table
     */
    fun addOrUpdate(v: T): FormatTable<T>
}

