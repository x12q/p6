package com.qxdzbc.p6.ui.format.flyweight


object Flyweights {
    fun<T> wrap(attr: T): Flyweight<T> {
        return FlyweightImp(attr, 0)
    }
}
