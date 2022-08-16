package com.emeraldblast.p6.app.document.range

import com.emeraldblast.p6.app.document.cell.CellErrors
import com.emeraldblast.p6.common.exception.error.ErrorHeader
import com.emeraldblast.p6.common.exception.error.ErrorReport

object RangeErrors {
    val prefix = "UI_RangeErrors_"

    object InvalidRangeAddress {
        val header = ErrorHeader("${prefix}0", "Invalid range address")
        fun report(label: String): ErrorReport {
            return ErrorReport(
                header = header.setDescription("Invalid range address: ${label}")
            )
        }
    }
}
