package com.emeraldblast.p6.ui.format.pack

import com.emeraldblast.p6.app.common.utils.WithSize
import com.emeraldblast.p6.ui.format.FormatAttribute
import com.emeraldblast.p6.ui.format.marked.MarkedAttribute

interface AttributePack : FormatAttribute, WithSize {

    val allMarkedAttrs:Set<MarkedAttribute>
    val allAttrs:Set<FormatAttribute>
    override val size:Int

    /**
     * remove an attribute that is equal [attr]
     */
    fun remove(attr: MarkedAttribute): AttributePack

    fun add(attr: MarkedAttribute): AttributePack

    fun removeInvalidAttribute():AttributePack
}
