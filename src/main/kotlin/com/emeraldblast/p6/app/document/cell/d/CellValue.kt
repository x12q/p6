package com.emeraldblast.p6.app.document.cell.d

import com.emeraldblast.p6.app.document.cell.CellErrors
import com.emeraldblast.p6.app.document.range.Range
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.DocProtos.CellValueProto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

/**
 * A class that holds the value (value only, not including the formula) of a cell
 */
data class CellValue constructor(
    val number: Double? = null,
    val bool: Boolean? = null,
    val str: String? = null,
    val range: Range? = null,
    val cell: Cell? = null,
    val errorReport: ErrorReport? = null,
    val transErrorReport: ErrorReport? = null,
) {
    init {
        val nonNullCount = listOfNotNull(number, bool, str, errorReport, range, cell).size
        if (nonNullCount > 1) {
            throw IllegalArgumentException("There are $nonNullCount non-null values. CellValue can only hold 0 or 1 non-null value.")
        }
    }

    companion object {
        val empty = CellValue()
        fun fromRs(rs: Result<Any, ErrorReport>): CellValue {
            when (rs) {
                is Err -> return CellValue(errorReport = rs.error)
                is Ok -> {
                    return fromAny(rs.value)
                }
            }
        }

        fun fromAny(any: Any?): CellValue {
            if (any == null) {
                return empty
            }
            val i = any
            when (i) {
                is String -> return from(i)
                is Number -> return from(i.toDouble())
                is Boolean -> return from(i)
                is Range -> return from(i)
                is Cell -> return from(i)
                is ErrorReport -> return from(i)
                else -> {
                    return from(CellErrors.InvalidCellValue.report(i))
                }
            }
        }

        fun fromTransError(errorReport: ErrorReport): CellValue {
            return CellValue(transErrorReport = errorReport)
        }

        fun from(i: Cell): CellValue {
            return CellValue(cell = i)
        }

        fun from(i: Range): CellValue {
            return CellValue(range = i)
        }

        fun from(i: Number): CellValue {
            return CellValue(number = i.toDouble())
        }

        fun Number.toCellValue(): CellValue {
            return CellValue(number = this.toDouble())
        }

        fun from(i: Boolean): CellValue {
            return CellValue(bool = i)
        }

        fun Boolean.toCellValue(): CellValue {
            return CellValue(bool = this)
        }

        fun from(str: String): CellValue {
            return CellValue(str = str)
        }

        fun String.toCellValue(): CellValue {
            return CellValue(str = this)
        }

        fun from(i: ErrorReport): CellValue {
            return CellValue(errorReport = i)
        }

        fun ErrorReport.toCellValue(): CellValue {
            return CellValue(errorReport = this)
        }

        fun CellValueProto.toModel(): CellValue {
            if (this.hasBool()) {
                return this.bool.toCellValue()
            }
            if (this.hasNum()) {
                return this.num.toCellValue()
            }
            if (this.hasStr()) {
                return this.str.toCellValue()
            }
            return empty
        }
    }

    private val all = listOfNotNull(number, bool, str, errorReport, range, cell, transErrorReport)

    val currentValue: Any? get() = all.firstOrNull()

    val valueAfterRun: Any?
        get() {
            val o = all.firstOrNull()
            when (o) {
                is Range -> {
                    if (o.isCell) {
                        val rt = o.cells[0].valueAfterRun
                        return rt
                    } else {
                        return o
                    }
                }
                is Cell -> return o.valueAfterRun
                else -> return o
            }
        }

    val isBool get() = this.bool != null
    val isNumber get() = this.number != null
    val isStr get() = this.str != null
    val isErr get() = this.errorReport != null
    val isTranslatorErr get() = this.transErrorReport != null
    val isRange get() = this.range != null
    val isCell get() = cell != null

    fun setValue(any: Any?): CellValue {
        return fromAny(any)
    }

    /**
     * A cell content is only legal if it contains at most 1 non-null value
     */
    fun isLegal(): Boolean {
        return all.size <= 1
    }

    fun isEmpty(): Boolean {
        return all.isEmpty()
    }

    /**
     * @return: a string value that can be edited by cell editor
     */
    val editableValue: String?
        get() {
            // range, cell, and err report are only created by formula, therefore they can't be edited directly
            if (this.isRange || this.isCell || this.isErr || this.isTranslatorErr) {
                return null
            } else {
                return displayStr
            }
        }

    /**
     * @return a string value for displaying inside a cell. This is what the user see in the cell on a worksheet.
     */
    val displayStr: String
        get() {
            if (number != null) {
                // handle int number
                if (number.toInt().toDouble() == number) {
                    return number.toInt().toString()
                }
                return number.toString()
            }
            if (bool != null) {
                if (bool) {
                    return "TRUE"
                } else {
                    return "FALSE"
                }
            }
            if (str != null) {
                return str
            }
            if (errorReport != null) {
                return errorReport.toString()
            }
            if (transErrorReport != null) {
                return transErrorReport.toString()
            }
            if (cell != null) {
                return cell.displayValue
            }
            if (range != null) {
                if (range.isCell) {
                    val cell = range.cells[0]
                    return cell.valueAfterRun?.toString() ?: ""
                } else {
                    return "Range[${range.address.label}]"
                }
            }
            return ""
        }

    fun toProto(): CellValueProto {
        val rt = CellValueProto.newBuilder().let { bd ->
            number?.also { bd.setNum(it) }
            str?.also { bd.setStr(it) }
            bool?.also { bd.setBool(it) }
            bd
        }.build()
        return rt
    }
}
