package com.qxdzbc.common.flyweight


interface FlyweightTable<T> : Map<T, Flyweight<T>>{

    val flyweightMap:Map<T, Flyweight<T>>

    fun getFlyweight(v:T): Flyweight<T>?

    fun addFlyweight(i: Flyweight<T>): FlyweightTable<T>

    fun addAndGetFlyweight(v:T):Pair<FlyweightTable<T>, Flyweight<T>>

    fun reduceCount(v:T): FlyweightTable<T>
    fun increaseCount(v:T): FlyweightTable<T>

    /**
     * Change ref count of a flyweight of a [v] value
     */
    fun changeCountIfPossible(v:T, count:Int): FlyweightTable<T>

    fun removeFlyweight(v:T): FlyweightTable<T>

    fun removeAll(): FlyweightTable<T>

    /**
     * Try to add a value [v] as a marked attribute, update the respective marked attribute already exist, otherwise, create a new marked attribute and add it to this table
     */
    fun addOrUpdate(v: T): FlyweightTable<T>
}

