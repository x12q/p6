package com.qxdzbc.p6.ui.worksheet.cursor.state


import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddresses
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddressUtils
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.translator.partial_text_element_extractor.PartialFormulaTreeExtractor
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementVisitor
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.TextDifferImp
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorStateImp
import com.qxdzbc.p6.ui.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.worksheet.cursor.di.qualifiers.MainCellState
import com.qxdzbc.p6.ui.worksheet.cursor.thumb.state.ThumbState
import com.qxdzbc.p6.ui.worksheet.di.comp.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.*
import com.qxdzbc.p6.ui.worksheet.state.RangeConstraint
import com.qxdzbc.p6.ui.worksheet.state.WorksheetId
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(WsAnvilScope::class, boundType = CursorState::class)
data class CursorStateImp @Inject constructor(
    override val idMs: Ms<CursorId>,
    override val cellLayoutCoorsMapSt: St<@JvmSuppressWildcards Map<CellAddress, LayoutCoorWrapper>>,
    override val thumbStateMs: Ms<ThumbState>,
    @MainCellState
    val mainCellMs: Ms<CellAddress> = ms(CellAddresses.A1),
    override val cellEditorStateMs: Ms<CellEditorState>,
    @Init_MainRangeInCursor
    override val mainRange: RangeAddress? = null,
    @Init_FragmentedCellSetInCursor
    override val fragmentedCells: Set<@JvmSuppressWildcards CellAddress>,
    @Init_RangeAddressSetInCursor
    override val fragmentedRanges: Set<@JvmSuppressWildcards RangeAddress>,
    @Init_RangeConstraintInCursor
    override val rangeConstraint: RangeConstraint,
    @Init_ClipBoardRangeInCursor
    override val clipboardRange: RangeAddress,
    ) : BaseCursorState() {

    override val isEditing: Boolean by isEditingMs

    override val mainCellSt: St<CellAddress> get() = mainCellMs

    override val mainCell: CellAddress by mainCellMs

    override fun mainSelectionStr(against: CursorId): String {
        val c1 = mainRange?.rawLabel ?: mainCell.label
        val c2 = if(this.id == against){
            ""
        }else{
            "@${this.wsName}@${this.wbKey.name}"
        }
        return c1+c2
    }


    companion object {
        fun forTest(
            cursorIdMs: Ms<CursorId>,
            cellLayoutCoorsMapSt: St<Map<CellAddress, LayoutCoorWrapper>>,
            thumbStateMs: Ms<ThumbState>,
            mainCellMs:Ms<CellAddress> = ms(CellAddresses.A1)
        ): CursorStateImp {
            return CursorStateImp(
                idMs = cursorIdMs,
                cellLayoutCoorsMapSt = cellLayoutCoorsMapSt,
                mainCellMs = mainCellMs,
                rangeConstraint = WorksheetConstants.defaultRangeConstraint,
                mainRange = null,
                fragmentedCells = emptySet(),
                fragmentedRanges = emptySet(),
                cellEditorStateMs = ms(
                    CellEditorStateImp(
                        targetCell = mainCellMs.value,
                        targetCursorIdSt = cursorIdMs,
                        isOpenMs = false.toMs(),
                        treeExtractor = PartialFormulaTreeExtractor(),
                        visitor = TextElementVisitor(),
                        textDiffer = TextDifferImp()
                    )
                ),
                thumbStateMs = thumbStateMs,
                clipboardRange = RangeAddressUtils.InvalidRange,
            )
        }

        fun default2(
            worksheetIDMs: Ms<WorksheetId>,
            cellLayoutCoorsMapSt: St<Map<CellAddress, LayoutCoorWrapper>>,
            thumbStateMs: Ms<ThumbState>,
        ): CursorStateImp {
            return forTest(
                cursorIdMs = ms(CursorIdImp(wsStateIDMs = worksheetIDMs)),
                cellLayoutCoorsMapSt = cellLayoutCoorsMapSt,
                thumbStateMs = thumbStateMs
            )
        }
    }

    override val isEditingMs: St<Boolean> get() = cellEditorState.isOpenMs

    override fun setClipboardRange(rangeAddress: RangeAddress): CursorState {
        return this.copy(clipboardRange = rangeAddress)
    }

    override fun removeClipboardRange(): CursorState {
        return this.copy(clipboardRange = RangeAddressUtils.InvalidRange)
    }

    override fun containInClipboard(cellAddress: CellAddress): Boolean {
        return this.clipboardRange.contains(cellAddress)
    }

    override var id: CursorId by idMs

    override val cellLayoutCoorsMap: Map<CellAddress, LayoutCoorWrapper> by cellLayoutCoorsMapSt
    override var thumbState: ThumbState by thumbStateMs

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
            this.mainCellMs.value = newCellAddress
        }
        return this

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
