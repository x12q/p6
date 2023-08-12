package com.qxdzbc.p6.ui.format

import com.qxdzbc.common.WithSize
import com.qxdzbc.p6.document_data_layer.Shiftable
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.address.CRAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.proto.DocProtos.RangeAddressSetProto

/**
 * Contains a set of non-repeat range address, support:
 * - functions to add, remove and check for existence of ranges and cells.
 * - cross-checking with other [RangeAddressSet].
 *
 */
interface RangeAddressSet: WithSize, com.qxdzbc.p6.document_data_layer.Shiftable {
    val ranges: Set<RangeAddress>

    /**
     * Shift all the address in this set by a vector defined by [oldAnchorCell] and [newAnchorCell].
     * @return a new set, the old set is kept intact.
     */
    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): RangeAddressSet

    /**
     * Add multiple [RangeAddress] to this set. Produce a new set.
     */
    fun addRanges(rangeAddresses: Collection<RangeAddress>): RangeAddressSet
    /**
     * Add multiple [RangeAddress] to this set. Produce a new set.
     */
    fun addRanges(vararg rangeAddresses: RangeAddress): RangeAddressSet

    /**
     * Add a cell address to this set. Produce a new set.
     */
    fun addCell(cellAddress: CellAddress): RangeAddressSet

    fun contains(cellAddress: CellAddress): Boolean

    fun contains(rangeAddress: RangeAddress): Boolean

    fun hasIntersectionWith(rangeAddress: RangeAddress): Boolean

    fun removeCell(cellAddress: CellAddress): RangeAddressSet

    /**
     * Clip off all the part that intersect with [rangeAddress]
     */
    fun removeRange(rangeAddress: RangeAddress): RangeAddressSet

    /**
     * Get all intersection of ranges in this set with [rangeAddress]
     */
    fun getAllIntersectionWith(rangeAddress: RangeAddress):RangeAddressSet

    /**
     * Get all sub ranges that are in this set but not in [anotherSet]
     */
    fun getNotIn(anotherSet:RangeAddressSet):RangeAddressSet
    fun getNotIn(ranges:Collection<RangeAddress>):RangeAddressSet

    /**
     * Get all sub ranges that are in ranges of this set, but not in [rangeAddress]
     */
    fun getNotIn(rangeAddress: RangeAddress):RangeAddressSet
    fun toProto():RangeAddressSetProto

    companion object{
        /**
         * Generate a [RangeAddressSetImp] that contains only non-overlapping [RangeAddress]
         */
        fun random(numberRange:IntProgression):RangeAddressSetImp{
            return RangeAddressSetImp(
                numberRange.map {x->
                    RangeAddress(CellAddress(x,x))
                }
            )
        }
    }
}
