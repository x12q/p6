package com.qxdzbc.p6.app.document.cell

import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.app.document.cell.CellValue.Companion.toModel
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.DocProtos.CellProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellId.Companion.toShallowModel
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

data class CellImp(
    override val id: CellId,
    override val content: CellContent = CellContentImp(),
) : BaseCell(), WbWsSt by id {

    companion object{
        fun CellProto.toShallowModel(
            translator: P6Translator<ExUnit>
        ): CellImp {
            val sId = this.id.toShallowModel()
            if(this.hasFormula() && this.formula.isNotEmpty()){
                val transRs = translator.translate(formula)
                val content = CellContentImp.fromTransRs(transRs)
                return CellImp(
                    id = sId,
                    content = content
                )
            }else{
                return CellImp(
                    id = sId,
                    content = CellContentImp(
                        cellValueMs = this.value.toModel().toMs(),
                    )
                )
            }
        }

        fun CellProto.toModel(
            wbKeySt:St<WorkbookKey>,
            wsNameSt:St<String>,
            translator: P6Translator<ExUnit>,
        ): CellImp {
            if(this.hasFormula() && this.formula.isNotEmpty()){
                val transRs = translator.translate(formula)
                val content = CellContentImp.fromTransRs(transRs)
                return CellImp(
                    id = CellId(
                        address = this.id.cellAddress.toModel(),
                        wbKeySt=wbKeySt, wsNameSt=wsNameSt
                    ),
                    content = content
                )
            }else{
                return CellImp(
                    id = CellId(
                        address = this.id.cellAddress.toModel(),
                        wbKeySt=wbKeySt, wsNameSt=wsNameSt
                    ),
                    content = CellContentImp(
                        cellValueMs = this.value.toModel().toMs(),
                    )
                )
            }
        }
    }

    override fun shift(oldAnchorCell: GenericCellAddress<Int, Int>, newAnchorCell: GenericCellAddress<Int, Int>): Cell {
        val newAddress:CellAddress = address.shift(oldAnchorCell, newAnchorCell)
        val newContent: CellContent = content.shift(oldAnchorCell, newAnchorCell)
        return this.setAddress(newAddress).setContent(newContent)
    }

    override fun reRun(): Cell? {
        return reRunRs().component1()
    }

    override fun reRunRs(): Rse<Cell> {
        val c = content.reRunRs()
        val rt = c.map { this.copy(content = it) }
        return rt
    }

    override val address: CellAddress
        get() = id.address

    override fun setAddress(newAddress: CellAddress): Cell {
        return this.copy(id = id.setAddress(newAddress))
    }

    override fun setContent(content: CellContent): Cell {
        return this.copy(content = content)
    }

    override fun setCellValue(i: CellValue): Cell {
        val rs = this.content
            .setValue(i)
        return this.setContent(rs)
    }

    override fun toProto(): DocProtos.CellProto {
        val rt = CellProto.newBuilder()
            .setId(this.id.toProto())
            .apply {
                if (this@CellImp.isFormula) {
                    this.setFormula(this@CellImp.fullFormula)
                }
            }
            .setValue(this@CellImp.cellValueAfterRun.toProto())
            .build()
        return rt
    }

    override fun toString(): String {
        return "CellImp[address=${address},content=${content}]"
    }
}
