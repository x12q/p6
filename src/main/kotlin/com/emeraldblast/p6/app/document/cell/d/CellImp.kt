package com.emeraldblast.p6.app.document.cell.d

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.address.toModel
import com.emeraldblast.p6.app.document.cell.d.CellValue.Companion.toModel
import com.emeraldblast.p6.proto.DocProtos
import com.emeraldblast.p6.proto.DocProtos.CellProto
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
import java.util.*

data class CellImp(
    override val address: CellAddress,
    override val content: CellContent = CellContentImp()
) : BaseCell() {

    companion object {
        fun CellProto.toModel(translator: P6Translator<ExUnit>): Cell {
            if(this.hasFormula() && this.formula.isNotEmpty()){
                val transRs = translator.translate(formula)
                val content = CellContentImp.fromTransRs(transRs,formula)
                return CellImp(
                    address = address.toModel(),
                    content = content
                )
            }else{
                return CellImp(
                    address = address.toModel(),
                    content = CellContentImp(
                        cellValueMs = this.value.toModel().toMs(),
                    )
                )
            }
        }
    }

    override fun reRun(): Cell {
        content.reRun()
        return this
    }

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

    override fun toProto(): DocProtos.CellProto {
        val rt = CellProto.newBuilder()
            .setAddress(this.address.toProto())
            .apply {
                if (this@CellImp.isFormula) {
                    this.setFormula(this@CellImp.formula)
                }
            }
            .setValue(this@CellImp.cellValueAfterRun.toProto())
            .build()
        return rt
    }

    override fun equals(other: Any?): Boolean {
        if (other != null && other is Cell) {
            return this.address == other.address && this.content == other.content
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(this.address, this.content)
    }

    override fun toString(): String {
        return "DCellImp[address=${address},content=${content}]"
    }
}

