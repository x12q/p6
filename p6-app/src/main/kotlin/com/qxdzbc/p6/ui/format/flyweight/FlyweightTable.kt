package com.qxdzbc.p6.ui.format.flyweight


interface FlyweightTable<T> {

    val attrMap:Map<T, Flyweight<T>>

    fun getMarkedAttr(v:T): Flyweight<T>?

    fun addMarkedAttr(v:T, attr: Flyweight<T>): FlyweightTable<T>

    fun addAndGetMarkedAttr(v:T):Pair<FlyweightTable<T>, Flyweight<T>>

    fun reduceCountIfPossible(v:T): FlyweightTable<T>
    fun increaseCountIfPossible(v:T): FlyweightTable<T>
    fun changeCountIfPossible(v:T, count:Int): FlyweightTable<T>

    fun removeMarkedAttr(v:T): FlyweightTable<T>

    fun removeAll(): FlyweightTable<T>

    /**
     * Try to add a value [v] as a marked attribute, update the respective marked attribute already exist, otherwise, create a new marked attribute and add it to this table
     */
    fun addOrUpdate(v: T): FlyweightTable<T>
}

