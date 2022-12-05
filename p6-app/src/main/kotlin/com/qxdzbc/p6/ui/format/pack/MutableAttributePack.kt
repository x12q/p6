package com.qxdzbc.p6.ui.format.pack

import androidx.compose.ui.Modifier
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.format.FormatAttribute
import com.qxdzbc.p6.ui.format.FormatAttributes.mergeIntoModifier
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute

class MutableAttributePack(
    override val mSetMs: MutableSet<Ms<MarkedAttribute>> = mutableSetOf()

) : AttributePack {

    private val mSet: Set<MarkedAttribute> get() = mSetMs.map{it.value}.toSet()

    companion object{
        fun empty() = MutableAttributePack()
    }

    override val allMarkedAttrs: Set<MarkedAttribute> get() = mSet.toSet()
    override val allAttrs: Set<FormatAttribute> get() = allMarkedAttrs.map{it.attr}.toSet()
    override val modifier: Modifier get() = allMarkedAttrs.filter{it.isValid}.map{it.attr}.mergeIntoModifier()
    override val size:Int get() = mSetMs.filter { it.value.isValid && it.value.isCounterNotZero }.size

    override fun add(attr: MarkedAttribute): AttributePack {
       mSetMs.add(ms(attr))
        return this
    }

    override fun add(attrMs: Ms<MarkedAttribute>): AttributePack {
        mSetMs.add(attrMs)
        return this
    }

    override fun remove(attr: MarkedAttribute): AttributePack {
        mSetMs.removeIf{it.value == attr}
        return this
    }

    override fun remove(attr: FormatAttribute): AttributePack {
        mSetMs.removeIf{it.value.attr == attr}
        return this
    }

    override fun removeInvalidAttribute():AttributePack{
        this.mSetMs.removeIf { it.value.isNotValid }
        return this
    }
}
