package com.qxdzbc.p6.app.document.range.address

import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.proto.DocProtos.RangeAddressProto
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraint

interface RangeAddress : RangeConstraint, Shiftable {

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
     * @return a [RangeAddress] representing the intersection of this and [otherRangeAddress], return null if they do not overlap
     */
    fun intersect(otherRangeAddress: RangeAddress): RangeAddress?

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
     * @return the cell on the other end of the crossing line that starts with [cellAddress]
     * @throws Exception if the input cell is not one of the 4 vertices of this [RangeAddress]
     */
    fun takeCrossCell(cellAddress: CellAddress): CellAddress

    /**
     * Create a minimum range that contains both this range and [anotherRangeAddress].
     * The resulting range may create cells that does not belong to both of the original range
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
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): RangeAddress
}

/**
 * Factory method to create a [RangeAddress] from a list of [CellAddress]
 */
fun RangeAddress(cells: List<CellAddress>): RangeAddress {
    return RangeAddresses.fromManyCells(cells)
}

/**
 * Factory method to create a [RangeAddress] from 2 [CellAddress]
 */
fun RangeAddress(address1: CellAddress, address2: CellAddress): RangeAddress {
    return RangeAddresses.from2Cells(address1, address2)
}

/**
 * Factory method to create a [RangeAddress] from a single [CellAddress]
 */
fun RangeAddress(cellAddress: CellAddress): RangeAddress {
    return RangeAddresses.singleCell(cellAddress)
}

fun RangeAddress(colRange: IntRange, rowRange: IntRange): RangeAddress {
    return RangeAddressImp(
        topLeft = CellAddress(colRange.first, rowRange.first),
        botRight = CellAddress(colRange.last, rowRange.last)
    )
}

fun RangeAddress(label: String): RangeAddress {
    return RangeAddresses.fromLabel(label)
}
