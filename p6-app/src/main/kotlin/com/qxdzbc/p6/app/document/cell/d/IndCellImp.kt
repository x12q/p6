package com.qxdzbc.p6.app.document.cell.d

import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.app.document.cell.d.CellValue.Companion.toModel
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.DocProtos.CellProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

/**
 * a [Cell] implementation that is not tied to workbook key state nor worksheet name state.
 */
data class IndCellImp(
    override val address: CellAddress,
    override val content: CellContent = CellContentImp(),
) : BaseCell() {

    companion object {
//        fun CellProto.toIndModel(translator: P6Translator<ExUnit>): IndCellImp {
//            if(this.hasFormula() && this.formula.isNotEmpty()){
//                val transRs = translator.translate(formula)
//                val content = CellContentImp.fromTransRs(transRs)
//                return IndCellImp(
//                    address = id.cellAddress.toModel(),
//                    content = content
//                )
//            }else{
//                return IndCellImp(
//                    address = id.cellAddress.toModel(),
//                    content = CellContentImp(
//                        cellValueMs = this.value.toModel().toMs(),
//                    )
//                )
//            }
//        }
    }

    override fun shift(oldAnchorCell: GenericCellAddress<Int, Int>, newAnchorCell: GenericCellAddress<Int, Int>): Cell {
        val newAddress:CellAddress = address.shift(oldAnchorCell, newAnchorCell)
        val newContent:CellContent = content.shift(oldAnchorCell, newAnchorCell)
        return this.copy(
            address=newAddress,
            content = newContent
        )
    }

    override fun reRun(): Cell? {
        return reRunRs().component1()
    }

    override fun reRunRs(): Rse<Cell> {
        val c = content.reRunRs()
        val rt = c.map { this.copy(content = it) }
        return rt
    }

    override val id: CellId
        get() = throw UnsupportedOperationException()
    override val wbKeySt: St<WorkbookKey>
        get() = throw UnsupportedOperationException()
    override val wbKey: WorkbookKey
        get() = throw UnsupportedOperationException()
    override val wsNameSt: St<String>
        get() = throw UnsupportedOperationException()
    override val wsName: String
        get() = throw UnsupportedOperationException()

    override fun setAddress(newAddress: CellAddress): Cell {
        return this.copy(address = newAddress)
    }

    override fun setContent(content: CellContent): Cell {
        return this.copy(content = content)
    }

    override fun setCellValue(i: CellValue): Cell {
        val rs = this.content
            .setValue(i)
        return this.setContent(rs)
    }

    override fun toProto(): CellProto {
        throw UnsupportedOperationException()
    }

    override fun toString(): String {
        return "IndCellImp[address=${address},content=${content}]"
    }
}

