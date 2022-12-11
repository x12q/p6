package com.qxdzbc.common.flyweight


/**
 * A marked attribute is an attribute that is marked with additional information such as:
 *  - validity
 *  - reference counter
 *  this data is for managing attributes inside attribute table
 */
interface Flyweight<T> {
    val attr: T
    val refCount: Int
    fun upCounter(): Flyweight<T> = changeCounterBy(1)
    fun downCounter(): Flyweight<T> = changeCounterBy(-1)
    fun changeCounterBy(v:Int): Flyweight<T>
    val isCounterZero: Boolean get() = refCount == 0
    val isCounterNotZero: Boolean get() = refCount != 0
}
