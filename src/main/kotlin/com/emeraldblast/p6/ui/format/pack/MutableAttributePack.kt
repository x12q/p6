package com.emeraldblast.p6.ui.format.pack

import androidx.compose.ui.Modifier
import com.emeraldblast.p6.ui.format.FormatAttribute
import com.emeraldblast.p6.ui.format.FormatAttributes
import com.emeraldblast.p6.ui.format.marked.MarkedAttribute

class MutableAttributePack(
    private val mSet: MutableSet<MarkedAttribute> = mutableSetOf(),
) : AttributePack {
    companion object{
        fun empty() = MutableAttributePack()
    }

    override val allMarkedAttrs: Set<MarkedAttribute> get() = mSet.toSet()
    override val allAttrs: Set<FormatAttribute> get() = allMarkedAttrs.map{it.attr}.toSet()
    override val modifier: Modifier get() = FormatAttributes.merge(allMarkedAttrs.map{it.attr})
    override val size:Int get() = mSet.size

    override fun add(attr: MarkedAttribute): AttributePack {
       mSet.add(attr)
        return this
    }
    override fun remove(attr: MarkedAttribute): AttributePack {
        mSet.remove((attr))
        return this
    }

    override fun removeInvalidAttribute():AttributePack{
        this.mSet.removeIf { it.isNotValid }
        return this
    }
}
