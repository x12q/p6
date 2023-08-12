package com.qxdzbc.p6.document_data_layer.cell

import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.CellId.Companion.toModelDM
import com.qxdzbc.p6.document_data_layer.cell.CellId.Companion.toShallowModel
import com.qxdzbc.p6.document_data_layer.cell.CellValue.Companion.toModel
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.address.CRAddress
import com.qxdzbc.p6.document_data_layer.cell.address.toModel
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos.CellProto
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM.Companion.toModelDM
import com.qxdzbc.p6.rpc.cell.msg.CellDM
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import kotlin.jvm.Throws

/**
 * Standard worksheet cell used in the app.
 * Logic of a cell:
 * - when a cell runs, it does not change, the [content] will change internally.
 */
data class CellImp(
    override val id: CellId,
    override val content: CellContent = CellContentImp.empty,
    override val externalEvalError: ErrorReport? = null,
    override val cachedDisplayText: String = "",
) : BaseCell(), WbWsSt by id {

    override fun setExternalEvalError(i: ErrorReport?): CellImp {
        return this.copy(externalEvalError = i)
    }

    override fun shift(oldAnchorCell: CRAddress<Int, Int>, newAnchorCell: CRAddress<Int, Int>): Cell {
        val newAddress: CellAddress = address.shift(oldAnchorCell, newAnchorCell)
        val newContent: CellContent = content.shift(oldAnchorCell, newAnchorCell)
        return this.setAddress(newAddress).setContent(newContent)
    }

    override fun reRunRs(): Rse<Cell> {
        val contentRs = content.reRunRs()
        val rt = contentRs
            .map { this.copy(content = it).evaluateDisplayText() }
        return rt
    }

    override val address: CellAddress
        get() = id.address

    @Throws(Throwable::class)
    override fun attemptToAccessDisplayText(): String {
        return this.content.displayText
    }

    override fun evaluateDisplayText(): Cell {
        try {
            val dt = attemptToAccessDisplayText()
            return this.copy(cachedDisplayText = dt)
        } catch (e: Throwable) {
            val newError0 = when (e) {
                is StackOverflowError -> {
                    CellErrors.OverflowError.report("overflow when trying to evaluate display text of cell at ${this.id.repStr()}")
                }

                else -> {
                    CommonErrors.ExceptionError.report(e)
                }
            }
            return this.copy(externalEvalError = newError0, cachedDisplayText = ErrorDisplayText.err)
        }
    }

    override fun setAddress(newAddress: CellAddress): Cell {
        return this.copy(id = id.setAddress(newAddress))
    }

    override fun setContent(content: CellContent): Cell {
        val newContent = CellContentImp(
            cellValueMs = ms(content.cellValue),
            exUnit = content.exUnit,
            originalText = content.originalText,
        )
        return this.copy(content = newContent)
    }

    override fun setCellValue(i: CellValue): Cell {
        val rs = this.content
            .setValueAndDeleteExUnit(i)
        return this.setContent(rs)
    }

    override fun toProto(): CellProto {
        val rt = CellProto.newBuilder()
            .setId(this.id.toProto())
            .setContent(this.content.toProto())
            .build()
        return rt
    }

    companion object {
        fun CellProto.toModelDM():CellDM{
            return CellDM(
                id = this.id.toModelDM(),
                content = this.content.toModelDM()
            )
        }

        /**
         * a shallow model is a model that is not attached to the app state
         */
        fun CellProto.toShallowModel(
            translator: P6Translator<ExUnit>
        ): CellImp {
            val sId = this.id.toShallowModel()

            val content = this.content

            if (content.hasFormula() && content.formula.isNotEmpty()) {
                val transRs = translator.translate(content.formula)
                val content = CellContentImp.fromTransRs(transRs, content.formula)
                return CellImp(
                    id = sId,
                    content = content
                )
            } else {
                val cv: CellValue = content.cellValue.toModel()
                return CellImp(
                    id = sId,
                    content = CellContentImp(
                        cellValueMs = cv.toMs(),
                        originalText = cv.editableValue
                    )
                )
            }
        }

        fun CellProto.toModel(
            wbKeySt: St<WorkbookKey>,
            wsNameSt: St<String>,
            translator: P6Translator<ExUnit>,
        ): CellImp {
            val content = this.content
            if (content.hasFormula() && content.formula.isNotEmpty()) {
                val rt = CellImp(
                    id = CellId(
                        address = this.id.cellAddress.toModel(),
                        wbKeySt = wbKeySt, wsNameSt = wsNameSt
                    ),
                    content = run {
                        val transRs = translator.translate(content.formula)
                        val ct = CellContentImp.fromTransRs(transRs, content.formula)
                        ct
                    }
                )
                return rt
            } else {
                val rt = CellImp(
                    id = CellId(
                        address = this.id.cellAddress.toModel(),
                        wbKeySt = wbKeySt, wsNameSt = wsNameSt
                    ),
                    content = run {
                        val cv = this.content.cellValue.toModel()
                        CellContentImp(
                            cellValueMs = cv.toMs(),
                            originalText = cv.editableValue
                        )
                    }
                )
                return rt
            }
        }
    }

}

