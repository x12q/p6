package com.qxdzbc.p6.ui.format.pack

import com.qxdzbc.common.WithSize
import com.qxdzbc.p6.ui.format.FormatAttribute
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute

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
