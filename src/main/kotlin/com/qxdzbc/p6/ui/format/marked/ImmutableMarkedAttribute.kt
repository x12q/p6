package com.qxdzbc.p6.ui.format.marked

import com.qxdzbc.p6.ui.format.FormatAttribute

/**
 * TODO not use for now
 */
data class ImmutableMarkedAttribute internal constructor(
    override val attr: FormatAttribute,
    override val isValid: Boolean,
    override val refCounter: Int,
) :
    MarkedAttribute {

    override val isNotValid: Boolean get() = !isValid

    override fun switch(): MarkedAttribute {
        return this.copy(isValid = !isValid)
    }

    override fun invalidate(): MarkedAttribute {
        return this.copy(isValid = false)
    }

    override fun changeCounterBy(v: Int): MarkedAttribute {
        return this.copy(refCounter = maxOf(0,refCounter+v))
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
