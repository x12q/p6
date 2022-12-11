package com.qxdzbc.common.flyweight


object Flyweights {
    fun<T> wrap(attr: T): Flyweight<T> {
        return FlyweightImp(attr, 0)
    }
}
