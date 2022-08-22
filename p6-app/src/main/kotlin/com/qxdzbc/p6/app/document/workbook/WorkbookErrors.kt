package com.qxdzbc.p6.app.document.workbook

import com.qxdzbc.p6.common.exception.error.ErrorHeader
import com.qxdzbc.p6.common.exception.error.ErrorReport

object WorkbookErrors {
    val prefix = "UI_WorkbookErrors_"

    object InvalidWorksheet {
        val header = ErrorHeader("${prefix}0", "invalid worksheet")
        fun report(wsName: String): ErrorReport {
            return ErrorReport(
                header = header.setDescription("Worksheet named \"${wsName}\" does not exist")
            )
        }

        fun report(i: Int): ErrorReport {
            return ErrorReport(
                header = header.setDescription("Worksheet at index \"${i}\" does not exist")
            )
        }
        fun reportWithDetail(detail:String?):ErrorReport{
            return (detail?.let { header.setDescription(detail) } ?: header).toErrorReport()
        }
    }

    object WorksheetAlreadyExist {
        val header = ErrorHeader("${prefix}1", "worksheet already exists")
        fun report(wsName: String): ErrorReport {
            return ErrorReport(
                header = header.setDescription("Worksheet named \"${wsName}\" already exists")
            )
        }
        fun report2(detail:String?):ErrorReport{
            return (detail?.let { header.setDescription(detail) } ?: header).toErrorReport()
        }
    }

    object IllegalSheetName {
        val header = ErrorHeader("${prefix}2", "Sheet name is illegal")
        fun report(detail: String): ErrorReport {
            return header
                .setDescription(detail)
                .toErrorReport()
        }
    }
}
