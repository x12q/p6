package com.qxdzbc.p6.app.document.cell.address

import com.qxdzbc.p6.app.common.utils.CellLabelNumberSystem
import com.qxdzbc.p6.app.document.range.address.RangeAddresses


/**
 * Col-row
 */
data class CR(
    val n: Int,
    val isFixed: Boolean = false,
) : Number() {
    companion object {
        fun fromLabel(label: String): CR? {
            val isCol = RangeAddresses.singleWholeColAddressPattern.matchEntire(label) != null
            if (isCol) {
                val isFixed = label[0] == '$'
                val n=if(isFixed){
                    CellLabelNumberSystem.labelToNumber(label.substring(1))
                }else{
                    CellLabelNumberSystem.labelToNumber(label)
                }
                return CR(n, isFixed)
            }

            val isRow = RangeAddresses.singleWholeRowAddressPattern.matchEntire(label) != null
            if (isRow) {
                val isFixed = label[0] == '$'
                val n = if(isFixed){
                    label.substring(1).toInt()
                }else{
                    label.toInt()
                }
                return CR(n, isFixed)
            }
            return null
        }
    }

    fun toColLabel(): String {
        if (isFixed) {
            return "\$${CellLabelNumberSystem.numberToLabel(this.n)}"
        } else {
            return CellLabelNumberSystem.numberToLabel(this.n)
        }
    }

    override fun toByte(): Byte {
        return n.toByte()
    }

    override fun toChar(): Char {
        return n.toChar()
    }

    override fun toDouble(): Double {
        return n.toDouble()
    }

    override fun toFloat(): Float {
        return n.toFloat()
    }

    override fun toInt(): Int {
        return n
    }

    override fun toLong(): Long {
        return n.toLong()
    }

    override fun toShort(): Short {
        return n.toShort()
    }
}
