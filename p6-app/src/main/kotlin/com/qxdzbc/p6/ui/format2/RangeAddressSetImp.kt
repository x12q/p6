package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.app.document.range.address.RangeAddresses.toModel
import com.qxdzbc.p6.proto.DocProtos.RangeAddressSetProto

data class RangeAddressSetImp(
    override val ranges: Set<RangeAddress>
) : RangeAddressSet {

    constructor(vararg rangeAddresses: RangeAddress) : this(setOf(*rangeAddresses))
    constructor(rangeAddresses: Collection<RangeAddress>) : this(rangeAddresses.toSet())

    override fun contains(cellAddress: CellAddress): Boolean {
        return ranges.firstOrNull { it.contains(cellAddress) } != null
    }

    override fun addRanges(rangeAddresses: Collection<RangeAddress>): RangeAddressSetImp {
        val newSet = RangeAddresses.exhaustiveMergeRanges(this.ranges + rangeAddresses).toSet()
        return this.copy(ranges = newSet)
    }

    override fun addRanges(vararg rangeAddresses: RangeAddress): RangeAddressSetImp {
        val newSet = RangeAddresses.exhaustiveMergeRanges(this.ranges + rangeAddresses).toSet()
        return this.copy(ranges = newSet)
    }

    override fun hasIntersectionWith(rangeAddress: RangeAddress): Boolean {
        return ranges.firstOrNull { it.hasIntersectionWith(rangeAddress) } != null
    }

    override fun removeCell(cellAddress: CellAddress): RangeAddressSetImp {
        val brokenRanges = ranges.flatMap {
            it.removeCell(cellAddress)
        }
        val newSet = RangeAddresses.exhaustiveMergeRanges(brokenRanges).toSet()
        return this.copy(ranges = newSet)
    }

    /**
     * Clip off all the ranges that intersect with [rangeAddress]
     */
    override fun removeRange(rangeAddress: RangeAddress): RangeAddressSetImp {
        val clippedRanges = ranges.flatMap {
            it.getNotIn(rangeAddress)
        }
        val newSet = RangeAddresses.exhaustiveMergeRanges(clippedRanges).toSet()
        return this.copy(ranges = newSet)
    }

    override fun getAllIntersectionWith(rangeAddress: RangeAddress): RangeAddressSet {
        return RangeAddressSetImp(
            this.ranges.mapNotNull {
                it.intersect(rangeAddress)
            }.toSet()
        )
    }


    override fun getNotIn(anotherSet: RangeAddressSet): RangeAddressSet {
        return getNotIn(anotherSet.ranges)
    }

    override fun getNotIn(ranges: Collection<RangeAddress>): RangeAddressSet {
        val rt = ranges.fold(this){acc:RangeAddressSet,ra->
            acc.getNotIn(ra)
        }
        return rt
    }

    override fun getNotIn(rangeAddress: RangeAddress): RangeAddressSet {
        val rt = RangeAddressSetImp(this.ranges.flatMap {
            it.getNotIn(rangeAddress)
        }.toSet())
        return rt
    }

    override fun addCell(cellAddress: CellAddress): RangeAddressSetImp {
        val newSet = RangeAddresses.exhaustiveMergeRanges(ranges + RangeAddress(cellAddress)).toSet()
        return this.copy(ranges = newSet)
    }

    override val size: Int
        get() = ranges.size

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): RangeAddressSetImp {
        return this.copy(ranges=this.ranges.map{it.shift(oldAnchorCell, newAnchorCell)}.toSet())
    }

    override fun toProto():RangeAddressSetProto{
        return RangeAddressSetProto.newBuilder()
            .addAllRanges(this.ranges.map{it.toProto()})
            .build()
    }

    companion object{
        /**
         * generate a range address set of disjoint range address
         */
        fun random(numberRange:IntProgression):RangeAddressSetImp{
            return RangeAddressSetImp(
                numberRange.map {x->
                    RangeAddress(CellAddress(x,x))
                }
            )
        }

        fun RangeAddressSetProto.toModel():RangeAddressSetImp{
            return RangeAddressSetImp(
                this.rangesList.map{it.toModel()}.toSet()
            )
        }
    }
}
