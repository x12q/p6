package com.emeraldblast.p6.ui.format.pack

import androidx.compose.ui.Modifier
import com.emeraldblast.p6.ui.format.FormatAttribute
import com.emeraldblast.p6.ui.format.FormatAttributes
import com.emeraldblast.p6.ui.format.marked.MarkedAttribute

class ImmutableAttributePack(
    private val mSet: Set<MarkedAttribute> = setOf(),
) : AttributePack {

    companion object{
        val empty = ImmutableAttributePack()
    }
    override val allMarkedAttrs: Set<MarkedAttribute> get() = mSet.toSet()
    override val allAttrs: Set<FormatAttribute> get() = allMarkedAttrs.map{it.attr}.toSet()
    override val modifier: Modifier get() = FormatAttributes.merge(allMarkedAttrs.map{it.attr})
    override val size:Int get() = mSet.size

    override fun add(attr: MarkedAttribute): AttributePack {
        return ImmutableAttributePack(mSet+attr)
    }
    override fun remove(attr: MarkedAttribute): AttributePack {
        return ImmutableAttributePack(mSet - attr)
    }

    override fun removeInvalidAttribute():AttributePack{
        val newSet = mSet.filter { it.isValid }.toSet()
        return ImmutableAttributePack(newSet)
    }
}
