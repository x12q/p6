package com.qxdzbc.common.flyweight


/**
 * A marked attribute is an attribute that is marked with additional information such as:
 *  - validity
 *  - reference counter
 *  this data is for managing attributes inside attribute table
 */
interface Flyweight<T> {
    val value: T
    val refCount: Int
    fun increaseRefCount(): Flyweight<T> = changeRefCountBy(1)
    fun reduceRefCount(): Flyweight<T> = changeRefCountBy(-1)
    fun changeRefCountBy(v:Int): Flyweight<T>
    val isCounterZero: Boolean get() = refCount == 0
    val isCounterNotZero: Boolean get() = refCount != 0
}
