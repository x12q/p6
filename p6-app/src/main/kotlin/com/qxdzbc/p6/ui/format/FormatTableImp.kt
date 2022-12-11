package com.qxdzbc.p6.ui.format

import com.qxdzbc.p6.ui.format.marked.MarkedAttribute
import com.qxdzbc.p6.ui.format.marked.MarkedAttributes
import com.qxdzbc.p6.ui.format.attr.FormatAttributeImp

data class FormatTableImp<T>(
    override val attrMap: Map<T, MarkedAttribute<T>> = emptyMap()
) : FormatTable<T> {

    override fun getMarkedAttr(v: T): MarkedAttribute<T>? {
        return attrMap[v]
    }

    override fun addMarkedAttr(v: T, attr: MarkedAttribute<T>): FormatTableImp<T> {
        return this.copy(
            attrMap = attrMap + (v to attr)
        )
    }

    override fun removeMarkedAttr(v: T): FormatTableImp<T> {
        return this.copy(
            attrMap = attrMap - v
        )
    }

    override fun addAndGetMarkedAttr(v: T): Pair<FormatTableImp<T>, MarkedAttribute<T>> {
        val rt = internalAddOrUpdate(v){newAttrMs,newTable->
            Pair(newTable,newAttrMs)
        }
        return rt
    }

    override fun addOrUpdate(v: T): FormatTableImp<T> {
        val rt = internalAddOrUpdate(v){_,newTable->
            newTable
        }
        return rt
    }

    private fun <R> internalAddOrUpdate(
        v: T,
        returnFunction: (ms: MarkedAttribute<T>, newTable: FormatTableImp<T>) -> R
    ): R {
        val rt = this.getMarkedAttr(v)?.let {
            val newAttr = it.upCounter()
            returnFunction(newAttr, this.copy(attrMap = attrMap + (v to newAttr)))
        } ?: run {
            val newAttrMs = MarkedAttributes.wrap(FormatAttributeImp(v)).upCounter()
            val newTable = this.addMarkedAttr(v, newAttrMs)
            returnFunction(newAttrMs,newTable)
        }
        return rt
    }

    override fun reduceCountIfPossible(v: T): FormatTable<T> {
        return this.changeCountIfPossible(v, -1)
    }

    override fun increaseCountIfPossible(v: T): FormatTable<T> {
        return this.changeCountIfPossible(v, 1)
    }

    override fun changeCountIfPossible(v: T, count: Int): FormatTable<T> {
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

    override fun removeAll(): FormatTableImp<T> {
        return this.copy(
            attrMap = attrMap.filter { it.value.isCounterNotZero }
        )
    }
}
