package com.qxdzbc.p6.ui.format

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute
import com.qxdzbc.p6.ui.format.marked.MarkedAttributes
import com.qxdzbc.p6.ui.format.text_attr.FormatAttributeImp

data class FormatTableImp<T>(
    override val floatAttrMap: Map<T, Ms<MarkedAttribute<T>>> = emptyMap()
) : FormatTable<T> {

    override fun getMarkedAttr(v: T): Ms<MarkedAttribute<T>>? {
        return floatAttrMap[v]
    }

    override fun addMarkedAttr(v: T, attr: Ms<MarkedAttribute<T>>): FormatTableImp<T> {
        return this.copy(
            floatAttrMap = floatAttrMap + (v to attr)
        )
    }

    override fun removeMarkedAttr(v: T): FormatTableImp<T> {
        return this.copy(
            floatAttrMap = floatAttrMap - v
        )
    }

    override fun add(v: T): Pair<FormatTableImp<T>, Ms<MarkedAttribute<T>>> {
        val rt = this.getMarkedAttr(v)?.let {
            it.value = it.value.upCounter()
            Pair(this,it)
        } ?: run {
            val newAttrMs = MarkedAttributes.wrap(FormatAttributeImp(v)).upCounter().toMs()
            val newTable = this.addMarkedAttr(v,newAttrMs)
            Pair(newTable,newAttrMs)
        }
        return rt
    }

    override fun reduceCountIfPossible(v: T): FormatTable<T> {
        return this.changeCountIfPossible(v,1)
    }

    override fun increaseCountIfPossible(v: T): FormatTable<T> {
        return this.changeCountIfPossible(v,-1)
    }

    override fun changeCountIfPossible(v: T, count: Int): FormatTable<T> {
        val rt = getMarkedAttr(v)?.let {
            val newAttr = it.value.changeCounterBy(count)
            if(newAttr.isCounterZero){
                this.removeMarkedAttr(v)
            }else{
                it.value = newAttr
                this
            }
        } ?: this
        return rt
    }

    override fun cleanUp(): FormatTableImp<T> {
        return this.copy(
            floatAttrMap = floatAttrMap.filter { it.value.value.isCounterNotZero }
        )
    }
}
