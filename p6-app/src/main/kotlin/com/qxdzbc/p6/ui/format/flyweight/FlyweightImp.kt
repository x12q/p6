package com.qxdzbc.p6.ui.format.flyweight


data class FlyweightImp<T> internal constructor(
    override val attr: T,
    override val refCount: Int,
) : Flyweight<T> {

    override fun changeCounterBy(v: Int): FlyweightImp<T> {
        return this.copy(refCount = maxOf(0,refCount+v))
    }
}
