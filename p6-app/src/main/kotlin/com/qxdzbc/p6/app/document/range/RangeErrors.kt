package com.qxdzbc.p6.app.document.range

import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.SingleErrorReport

object RangeErrors {
    val prefix = "UI_RangeErrors_"

    object InvalidRangeAddress {
        val header = ErrorHeader("${prefix}0", "Invalid range address")
        fun report(label: String): SingleErrorReport {
            return SingleErrorReport(
                header = header.setDescription("Invalid range address: ${label}")
            )
        }
    }
}
