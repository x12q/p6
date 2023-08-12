package com.qxdzbc.p6.document_data_layer.range.address

import com.qxdzbc.p6.document_data_layer.Shiftable
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.address.CRAddress
import com.qxdzbc.p6.proto.DocProtos.RangeAddressProto
import com.qxdzbc.p6.ui.worksheet.state.RangeConstraint

interface RangeAddress : RangeConstraint, com.qxdzbc.p6.document_data_layer.Shiftable {

    fun nextLockState():RangeAddress

    val cellIterator: Iterator<CellAddress>

    val label: String
    val rawLabel: String

    val topLeft: CellAddress
    val botRight: CellAddress
    val topRight: CellAddress
    val botLeft: CellAddress

    fun setTopLeft(i:CellAddress):RangeAddress
    fun setBotRight(i:CellAddress):RangeAddress

    /**
     * lock the whole range address
     */
    fun lock(): RangeAddress

    /**
     * unlock the whole range address
     */
    fun unLock(): RangeAddress

    /**
     * a valid range is a range consisting of only valid cells.
     */
    fun isValid(): Boolean

    /**
     * @return a [RangeAddress] representing the intersection of this RangeAddress and [otherRangeAddress], return null if they do not overlap
     */
    fun intersect(otherRangeAddress: RangeAddress): RangeAddress?

    /**
     * @return a list of sub range address that are in this RangeAddress but not in the input [rangeAddress] (the difference)
     */
    fun getNotIn(rangeAddress: RangeAddress):List<RangeAddress>

    /**
     * Remove a cell from this range if possible, effectively breaking a range into smaller ranges
     * @return a list of [RangeAddress] resulting from the remove action
     */
    fun removeCell(cellAddress: CellAddress): List<RangeAddress>

    fun isRow(): Boolean
    fun isCol(): Boolean
    fun isCell(): Boolean

    /**
     * Eg: if the input == topLeft, then return botRight
     *
     * if the input == botRight, then return topLeft
     * @return the cell on the other end of the crossing line that starts with [vertex]
     * @throws Exception if the input cell is not one of the 4 vertices of this [RangeAddress]
     */
    fun takeCrossCell(vertex: CellAddress): CellAddress

    /**
     * Create a minimum range that contains both this range and [anotherRangeAddress].
     * The resulting range may contain cells that does not belong to both of the original range
     */
    fun mergeWith(anotherRangeAddress: RangeAddress): RangeAddress

    /**
     * expand this range to the smallest range that contains the old range and [cellAddress].
     * The resulting range may contain other unwanted range.
     * Eg: RangeAddress("A1:B2").expand(CellAddress("K3") will contain cells of C,D,E column.
     */
    fun expand(cellAddress: CellAddress): RangeAddress

    /**
     * Expand this range with a [CellAddress].
     * The resulting range contains strictly only the original and the new cell.
     * If that is not possible, return null.
     */
    fun strictMerge(cellAddress: CellAddress): RangeAddress?

    /**
     * Attempt to merge this range with another range into a new (rectangular) range that strictly contains the two original range, nothing else. Return null if unable to.
     */
    fun strictMerge(anotherRange: RangeAddress): RangeAddress?

    fun toProto(): RangeAddressProto
    fun getCellAddressCycle(cellAddress: CellAddress): CellAddress

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): RangeAddress

    fun hasIntersectionWith(rangeAddress: RangeAddress): Boolean

    companion object {
        fun random(colRange:IntRange=1 .. 20, rowRange:IntRange = 1 .. 20):RangeAddress{
            return RangeAddress(CellAddress.random(colRange,rowRange),CellAddress.random(colRange,rowRange))
        }
    }
}

/**
 * Factory method to create a [RangeAddress] from a list of [CellAddress]
 */
fun RangeAddress(cells: List<CellAddress>): RangeAddress {
    return RangeAddressUtils.rangeForMultiCells(cells)
}

/**
 * Factory method to create a [RangeAddress] from 2 [CellAddress]
 */
fun RangeAddress(address1: CellAddress, address2: CellAddress): RangeAddress {
    return RangeAddressUtils.rangeFor2Cells(address1, address2)
}

/**
 * Factory method to create a [RangeAddress] from a single [CellAddress]
 */
fun RangeAddress(cellAddress: CellAddress): RangeAddress {
    return RangeAddressUtils.rangeFromSingleCell(cellAddress)
}

fun RangeAddress(colRange: IntRange, rowRange: IntRange): RangeAddress {
    return RangeAddressImp(
        topLeft = CellAddress(colRange.first, rowRange.first),
        botRight = CellAddress(colRange.last, rowRange.last)
    )
}

fun RangeAddress(label: String): RangeAddress {
    return RangeAddressUtils.rangeFromLabel(label)
}
