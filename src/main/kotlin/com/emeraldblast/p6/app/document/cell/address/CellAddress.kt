package com.emeraldblast.p6.app.document.cell.address

import com.emeraldblast.p6.app.common.utils.CellLabelNumberSystem
import com.emeraldblast.p6.proto.DocProtos

/**
 * factory method to create a standard cell address from col, row indices
 */
fun CellAddress(col: Int, row: Int): CellAddress {
    return CellAddresses.fromIndices(col, row)
}

/**
 * factory method to create a standard cell address from a cell label
 */
fun CellAddress(label: String): CellAddress {
    return CellAddresses.fromLabel(label)
}

interface CellAddress : GenericCellAddress<Int, Int> {
    override val colIndex: Int
    override val rowIndex: Int

    /**
     * Shift this address using the vector defined by: [oldAnchorCell] --> [newAnchorCell].
     * See the test for more detail
     */
    fun shift(oldAnchorCell:CellAddress, newAnchorCell:CellAddress):CellAddress
    fun increaseRowBy(v: Int): CellAddress
    fun increaseColBy(v: Int): CellAddress


    /**
     * decrease row index by 1
     */
    fun upOneRow(): CellAddress {
        return this.increaseRowBy(-1)
    }

    /**
     * increase row index by 1
     */
    fun downOneRow(): CellAddress {
        return this.increaseRowBy(1)
    }

    /**
     * increase col index by 1
     */
    fun rightOneCol(): CellAddress {
        return this.increaseColBy(1)
    }

    /**
     * decrease col index by 1
     */
    fun leftOneCol(): CellAddress {
        return this.increaseColBy(-1)
    }

    fun toRawLabel(): String {
        val colLabel: String = CellLabelNumberSystem.numberToLabel(colIndex)
        return "${colLabel}${rowIndex}"
    }

    fun toLabel(): String {
        val colLabel: String = CellLabelNumberSystem.numberToLabel(colIndex)
        return "@${colLabel}${rowIndex}"
    }

    fun toProto(): DocProtos.CellAddressProto
    fun isValid(): Boolean {
        return this.colIndex >= 1 && this.rowIndex >= 1
    }
    fun isNotValid():Boolean{
        return !this.isValid()
    }
}
