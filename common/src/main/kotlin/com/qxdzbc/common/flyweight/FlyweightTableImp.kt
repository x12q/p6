package com.qxdzbc.common.flyweight

data class FlyweightTableImp<T>(
    override val flyweightMap: Map<T, Flyweight<T>> = emptyMap()
) : FlyweightTable<T>,
    Map<T, Flyweight<T>> by flyweightMap
{

    override fun getFlyweight(v: T): Flyweight<T>? {
        return flyweightMap[v]
    }

    override fun addFlyweight(i: Flyweight<T>): FlyweightTableImp<T> {
        return this.copy(
            flyweightMap = flyweightMap + (i.value to i)
        )
    }

    override fun removeFlyweight(v: T): FlyweightTableImp<T> {
        return this.copy(
            flyweightMap = flyweightMap - v
        )
    }

    override fun addAndGetFlyweight(v: T): Pair<FlyweightTableImp<T>, Flyweight<T>> {
        val rt = internalAddOrUpdate(v) { newAttrMs, newTable ->
            Pair(newTable, newAttrMs)
        }
        return rt
    }

    override fun addOrUpdate(v: T): FlyweightTableImp<T> {
        val rt = internalAddOrUpdate(v) { _, newTable ->
            newTable
        }
        return rt
    }

    private fun <R> internalAddOrUpdate(
        v: T,
        returnFunction: (ms: Flyweight<T>, newTable: FlyweightTableImp<T>) -> R
    ): R {
        val rt = this.getFlyweight(v)?.let {
            val newAttr = it.increaseRefCount()
            returnFunction(newAttr, this.copy(flyweightMap = flyweightMap + (v to newAttr)))
        } ?: run {
            val newAttrMs = Flyweights.wrap(v).increaseRefCount()
            val newTable = this.addFlyweight(newAttrMs)
            returnFunction(newAttrMs, newTable)
        }
        return rt
    }

    override fun reduceCount(v: T): FlyweightTable<T> {
        return this.changeCountIfPossible(v, -1)
    }

    override fun increaseCount(v: T): FlyweightTable<T> {
        val fw = getFlyweight(v) ?: Flyweights.wrap(v)
        val fw2 = fw.increaseRefCount()
        val rt = this.addFlyweight(fw2)
        return rt
    }

    override fun changeCountIfPossible(v: T, count: Int): FlyweightTable<T> {
        val rt = getFlyweight(v)?.let {
            val newFw = it.changeRefCountBy(count)
            if (newFw.isCounterZero) {
                this.removeFlyweight(v)
            } else {
                this.addFlyweight(newFw)
            }
        } ?: this
        return rt
    }

    override fun removeAll(): FlyweightTableImp<T> {
        return this.copy(
            flyweightMap = flyweightMap.filter { it.value.isCounterNotZero }
        )
    }
}
