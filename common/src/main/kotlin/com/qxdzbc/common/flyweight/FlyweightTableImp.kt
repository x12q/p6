package com.qxdzbc.common.flyweight

data class FlyweightTableImp<T>(
    override val attrMap: Map<T, Flyweight<T>> = emptyMap()
) : FlyweightTable<T> {

    override fun getMarkedAttr(v: T): Flyweight<T>? {
        return attrMap[v]
    }

    override fun addMarkedAttr(v: T, attr: Flyweight<T>): FlyweightTableImp<T> {
        return this.copy(
            attrMap = attrMap + (v to attr)
        )
    }

    override fun removeMarkedAttr(v: T): FlyweightTableImp<T> {
        return this.copy(
            attrMap = attrMap - v
        )
    }

    override fun addAndGetMarkedAttr(v: T): Pair<FlyweightTableImp<T>, Flyweight<T>> {
        val rt = internalAddOrUpdate(v){newAttrMs,newTable->
            Pair(newTable,newAttrMs)
        }
        return rt
    }

    override fun addOrUpdate(v: T): FlyweightTableImp<T> {
        val rt = internalAddOrUpdate(v){_,newTable->
            newTable
        }
        return rt
    }

    private fun <R> internalAddOrUpdate(
        v: T,
        returnFunction: (ms: Flyweight<T>, newTable: FlyweightTableImp<T>) -> R
    ): R {
        val rt = this.getMarkedAttr(v)?.let {
            val newAttr = it.upCounter()
            returnFunction(newAttr, this.copy(attrMap = attrMap + (v to newAttr)))
        } ?: run {
            val newAttrMs = Flyweights.wrap(v).upCounter()
            val newTable = this.addMarkedAttr(v, newAttrMs)
            returnFunction(newAttrMs,newTable)
        }
        return rt
    }

    override fun reduceCountIfPossible(v: T): FlyweightTable<T> {
        return this.changeCountIfPossible(v, -1)
    }

    override fun increaseCountIfPossible(v: T): FlyweightTable<T> {
        return this.changeCountIfPossible(v, 1)
    }

    override fun changeCountIfPossible(v: T, count: Int): FlyweightTable<T> {
        val rt = getMarkedAttr(v)?.let {
            val newAttr = it.changeCounterBy(count)
            if (newAttr.isCounterZero) {
                this.removeMarkedAttr(v)
            } else {
                this.addMarkedAttr(v,newAttr)
            }
        } ?: this
        return rt
    }

    override fun removeAll(): FlyweightTableImp<T> {
        return this.copy(
            attrMap = attrMap.filter { it.value.isCounterNotZero }
        )
    }
}
