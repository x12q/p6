package com.qxdzbc.p6.ui.document.worksheet.cursor.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.state.app_state.CellEditorStateMs
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorStateImp
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraint
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId
import dagger.assisted.AssistedInject
import com.qxdzbc.p6.di.state.ws.*
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import dagger.assisted.Assisted


data class CursorStateImp @AssistedInject constructor(
    @Assisted("1")
    override val idMs: Ms<CursorStateId>,
    //=============================================//
    @CellEditorStateMs
    override val cellEditorStateMs: Ms<CellEditorState>,
    @NullRangeAddress
    override val mainRange: RangeAddress? = null,
    @EmptyCellAddressSet
    override val fragmentedCells: Set<@JvmSuppressWildcards CellAddress> = emptySet(),
    @EmptyRangeAddressSet
    override val fragmentedRanges: Set<@JvmSuppressWildcards RangeAddress> = emptySet(),
    @DefaultTopLeftCellAddress
    override val mainCell: CellAddress = CellAddresses.A1,
    @DefaultRangeConstraint
    override val rangeConstraint: RangeConstraint = P6R.worksheetValue.defaultRangeConstraint,
    @DefaultClipBoardRange
    override val clipboardRange: RangeAddress = RangeAddresses.InvalidRange,
) : BaseCursorState() {
    override val isEditing: Boolean by isEditingMs
    companion object {
        fun default(
            cursorIdMs: Ms<CursorStateId>
        ): CursorStateImp {
            val mainCell = CellAddress(1, 1)
            return CursorStateImp(
                mainCell = mainCell,
                rangeConstraint = P6R.worksheetValue.defaultRangeConstraint,
                mainRange = null,
                fragmentedCells = emptySet(),
                fragmentedRanges = emptySet(),
                cellEditorStateMs = ms(
                    CellEditorStateImp(
                        targetCell = mainCell,
                        targetCursorIdSt = cursorIdMs,
                        isActiveMs = false.toMs(),
                    )
                ),
                idMs = cursorIdMs
            )
        }

        fun default2(
            worksheetIDMs: Ms<WorksheetId>,
        ): CursorStateImp {
            return default(
                ms(CursorIdImp(wsStateIDMs = worksheetIDMs))
            )
        }

        internal fun exhaustiveMergeCellsIntoOneRange(
            cells: List<CellAddress>,
            rangeAddress: RangeAddress
        ): Pair<RangeAddress, List<CellAddress>> {
            val cRanges = cells.map { RangeAddress(it) }
            val (resultRange, unUsedRanges) = exhaustiveMergeRangesIntoOneRange(cRanges, rangeAddress)
            return Pair(
                resultRange,
                unUsedRanges.map { it.topLeft }
            )
        }

        /**
         * attempt to exhaustively merge a list of ranges into one range
         */
        internal fun exhaustiveMergeRangesIntoOneRange(
            ranges: List<RangeAddress>,
            rangeAddress: RangeAddress
        ): Pair<RangeAddress, List<RangeAddress>> {
            var iterRange = rangeAddress
            val unUsedRanges = mutableListOf<RangeAddress>()
            var candidateRanges = ranges
            var merged = false
            while (true) {
                for (range in candidateRanges) {
                    val tr = iterRange.strictMerge(range)
                    if (tr != null) {
                        iterRange = tr
                        merged = true
                    } else {
                        unUsedRanges.add(range)
                    }
                }
                if (!merged) {
                    break
                }
                candidateRanges = unUsedRanges.map { it }
                unUsedRanges.clear()
                merged = false
            }
            return Pair(iterRange, unUsedRanges)
        }

        internal fun exhaustiveMergeCell(cellList: List<CellAddress>): Pair<List<RangeAddress>, List<CellAddress>> {
            val r = exhaustiveMergeRanges(cellList.map { RangeAddress(it) })
            val unUsedCells = r.filter { it.isCell() }.map { it.topLeft }
            val resultRanges = r.filter { !it.isCell() }
            return Pair(resultRanges, unUsedCells)
        }

        internal fun exhaustiveMergeCellsToRanges(
            cells: List<CellAddress>,
            ranges: List<RangeAddress>
        ): Pair<List<RangeAddress>, List<CellAddress>> {
            val cellRanges = cells.map { RangeAddress(it) }
            val rs = exhaustiveMergeRanges(ranges + cellRanges)
            val unUsedCell = rs.filter { it.isCell() }.map { it.topLeft }
            val newRanges = rs.filter { it.isCell().not() }
            return Pair(newRanges, unUsedCell)
        }

        /**
         * attempt to merge a cell into a list of range.
         * If the cell is merged into a range in the list, proceed to exhaustively merge all the range if possible.
         * @return a list of ranges that cannot be further merged
         */
        internal fun exhaustiveMergeRanges(
            cellAddress: CellAddress,
            fragRanges: List<RangeAddress>
        ): Pair<Boolean, List<RangeAddress>> {
            val expandedRangeList = mutableListOf<RangeAddress>()
            var ignoreTheRest = false
            var cellWasConsumed = false
            for (range in fragRanges) {
                if (!ignoreTheRest) {
                    val expandedRange = range.strictMerge(cellAddress)
                    if (expandedRange != null) {
                        expandedRangeList.add(expandedRange)
                        cellWasConsumed = true
                        ignoreTheRest = true
                    } else {
                        expandedRangeList.add(range)
                    }
                } else {
                    expandedRangeList.add(range)
                }
            }
            return Pair(cellWasConsumed, exhaustiveMergeRanges(expandedRangeList))
        }

        /**
         * exhaustively merge a list of range.
         * @return a list of ranges that cannot be further merged
         */
        fun exhaustiveMergeRanges(rangeList: Collection<RangeAddress>): List<RangeAddress> {
            var l = rangeList
            while (true) {
                val newL = exhaustiveMergeRange_OneIteration(l.toList())
                if (l.size == newL.size) {
                    break
                } else {
                    l = newL
                }
            }
            return l.toList()
        }

        private fun exhaustiveMergeRange_OneIteration(rangeList: List<RangeAddress>): List<RangeAddress> {
            if (rangeList.isEmpty() || rangeList.size == 1) {
                return rangeList
            } else {
                val l = mutableListOf<RangeAddress>()
                val used = mutableSetOf<RangeAddress>()
                for ((x, r1) in rangeList.withIndex()) {
                    var merged = false
                    for (y in x + 1 until rangeList.size) {
                        val r2 = rangeList[y]
                        val r = r1.strictMerge(r2)
                        if (r != null) {
                            l.add(r)
                            used.add(r1)
                            used.add(r2)
                            merged = true
                            break
                        }
                    }
                    if (!merged && r1 !in used) {
                        l.add(r1)
                    }
                }
                return l
            }
        }
    }

    override val isEditingMs: St<Boolean> get()=cellEditorState.isActiveMs

    override fun setClipboardRange(rangeAddress: RangeAddress): CursorState {
        return this.copy(clipboardRange = rangeAddress)
    }

    override fun removeClipboardRange(): CursorState {
        return this.copy(clipboardRange = RangeAddresses.InvalidRange)
    }

    override fun containInClipboard(cellAddress: CellAddress): Boolean {
        return this.clipboardRange.contains(cellAddress)
    }

    override var id: CursorStateId by idMs

    override fun up(): CursorState {
        val newCellAddress = mainCell.upOneRow()
        if (rangeConstraint.contains(newCellAddress)) {
            return this
                .setMainCell(newCellAddress)
                .removeAllExceptMainCell()
        } else {
            return this
        }
    }

    override fun down(): CursorState {
        val newCellAddress = mainCell.downOneRow()
        if (rangeConstraint.contains(newCellAddress)) {
            return this
                .setMainCell(newCellAddress)
                .removeAllExceptMainCell()
        } else {
            return this
        }
    }

    override fun left(): CursorState {
        val newCellAddress = mainCell.leftOneCol()
        if (rangeConstraint.contains(newCellAddress)) {
            return this
                .setMainCell(newCellAddress)
                .removeAllExceptMainCell()
        } else {
            return this
        }
    }

    override fun right(): CursorState {
        val newCellAddress = mainCell.rightOneCol()
        if (rangeConstraint.contains(newCellAddress)) {
            return this
                .setMainCell(newCellAddress)
                .removeAllExceptMainCell()
        } else {
            return this
        }
    }

    override fun setMainCell(newCellAddress: CellAddress): CursorState {
        if (rangeConstraint.contains(newCellAddress) && mainCell != newCellAddress) {
            return this.copy(mainCell = newCellAddress)
        } else {
            return this
        }
    }

    override fun addFragRanges(rangeAddressList: Collection<RangeAddress>): CursorState {
        if (rangeAddressList.isEmpty()) {
            return this
        }
        return this.setFragRanges(this.fragmentedRanges + rangeAddressList)
    }

    override fun setMainRange(rangeAddress: RangeAddress?): CursorState {
        if (rangeAddress != null) {
            if (rangeConstraint.contains(rangeAddress)) {
                return this.copy(mainRange = rangeAddress)
            } else {
                return this
            }
        } else {
            if (this.mainRange != null) {
                return this.copy(mainRange = null)
            } else {
                return this
            }
        }
    }

    override fun setFragmentedCells(cells: Collection<CellAddress>): CursorState {
        val legalSelections = fragmentedCells.filter {
            this.rangeConstraint.contains(it)
        }
        return this.removeAllFragmentedCells().addFragCells(legalSelections)
    }

    override fun addFragCell(cellAddress: CellAddress): CursorState {
        val newCursor = this.copy(fragmentedCells = fragmentedCells + cellAddress)
        return newCursor
    }

    override fun addFragCells(cellAddressList: Collection<CellAddress>): CursorState {
        if (cellAddressList.isNotEmpty()) {
            val c: CursorState = this
            val rt = cellAddressList.fold(c) { acc, cellAddress ->
                acc.addFragCell(cellAddress)
            }
            return rt
        }
        return this
    }

    override fun removeFragCell(cellAddress: CellAddress): CursorState {
        if (cellAddress in this.fragmentedCells) {
            return this.copy(fragmentedCells = this.fragmentedCells - cellAddress)
        }
        return this
    }

    override fun removeAllFragmentedCells(): CursorState {
        if (this.fragmentedCells.isNotEmpty()) {
            return this.copy(fragmentedCells = emptySet())
        }
        return this
    }

    override fun addFragRange(rangeAddress: RangeAddress): CursorState {
        val notInMainRange = if (this.mainRange == null) {
            true
        } else {
            this.mainRange.contains(rangeAddress)
        }
        if ((rangeAddress !in this.fragmentedRanges) && notInMainRange) {
            val newCursor = this.copy(fragmentedRanges = fragmentedRanges + rangeAddress)
            return newCursor
        }
        return this
    }

    override fun removeFragRange(rangeAddress: RangeAddress): CursorState {
        if (rangeAddress in this.fragmentedRanges) {
            return this.copy(fragmentedRanges = fragmentedRanges - rangeAddress)
        }
        return this
    }

    override fun setFragRanges(ranges: Collection<RangeAddress>): CursorState {
        return this.copy(fragmentedRanges = ranges.toSet())
    }

    override fun removeAllSelectedFragRange(): CursorState {
        if (this.fragmentedRanges.isNotEmpty()) {
            return this.setFragRanges(emptySet())
        }
        return this
    }

    override val wbKeySt: St<WorkbookKey>
        get() = this.idMs.value.wbKeySt
    override val wsNameSt: St<String>
        get() = this.idMs.value.wsNameSt

    override val wbKey: WorkbookKey
        get() = this.idMs.value.wbKey
    override val wsName: String
        get() = this.idMs.value.wsName
}
