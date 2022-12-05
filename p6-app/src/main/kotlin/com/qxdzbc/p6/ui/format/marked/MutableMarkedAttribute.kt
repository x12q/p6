package com.qxdzbc.p6.ui.format.marked

import com.qxdzbc.p6.ui.format.FormatAttribute

data class MutableMarkedAttribute internal constructor(
    override val attr: FormatAttribute,
    private var v: Boolean,
    private var counter: Int,
) :
    MarkedAttribute {
    override val isValid: Boolean get() = v
    override val isNotValid: Boolean get() = !isValid
    override val refCounter: Int get() = counter
    override fun switch(): MarkedAttribute {
        v = !v
        return this
    }

    override fun invalidate(): MarkedAttribute {
        v = false
        return this
    }

    override fun validate(): MarkedAttribute {
        v = true
        return this
    }

    override fun changeCounterBy(v: Int): MarkedAttribute {
        counter = maxOf(0, refCounter + v)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other != null && other is MarkedAttribute) {
            return this.attr == other.attr
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return this.attr.hashCode()
    }

    override fun toString(): String {
        return "Marked ${this.isValid}: ${this.attr.toString()}"
    }
}
