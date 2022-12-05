package com.qxdzbc.p6.ui.format.pack

import androidx.compose.ui.Modifier
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.format.FormatAttribute
import com.qxdzbc.p6.ui.format.FormatAttributes.mergeIntoModifier
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute

data class ImmutableAttributePack(
    override val mSetMs: Set<Ms<MarkedAttribute>> = setOf()
) : AttributePack {

    companion object{
        val empty = ImmutableAttributePack()
    }
    private val mSet: Set<MarkedAttribute> = mSetMs.map{it.value}.toSet()
    override val allMarkedAttrs: Set<MarkedAttribute> get() = mSet
    override val allAttrs: Set<FormatAttribute> get() = allMarkedAttrs.map{it.attr}.toSet()
    override val modifier: Modifier get() = allMarkedAttrs.filter{it.isValid}.map{it.attr}.mergeIntoModifier()
    override val size:Int get() = mSetMs.filter { it.value.isValid && it.value.isCounterNotZero }.size

    override fun add(attr: MarkedAttribute): AttributePack {
        return this.copy(mSetMs=mSetMs + ms(attr))
    }

    override fun add(attrMs: Ms<MarkedAttribute>): AttributePack {
        return this.copy(mSetMs=mSetMs + attrMs)
    }

    override fun remove(attr: MarkedAttribute): AttributePack {
        return this.copy(mSetMs=mSetMs.filter{it.value!=attr}.toSet())
    }

    override fun remove(attr: FormatAttribute): AttributePack {
        return this.copy(mSetMs=mSetMs.filter{it.value.attr!=attr}.toSet())
    }

    override fun removeInvalidAttribute():AttributePack{
        val newSet = mSetMs.filter { it.value.isValid }.toSet()
        return ImmutableAttributePack(newSet)
    }
}
