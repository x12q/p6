package com.qxdzbc.p6.ui.document.workbook.state

import com.qxdzbc.p6.common.exception.error.ErrorHeader
import com.qxdzbc.p6.common.exception.error.ErrorReport

object WorkbookStateErrors {
    val prefix = "WorkbookStateContainerErrors_"
    object WorksheetStateNotExist{
        val header = ErrorHeader("${prefix}0", "Worksheet state does not exist")
        fun report(detail:String? = null): ErrorReport {
            return ErrorReport(
                header = detail?.let { header.setDescription(it) } ?: header
            )
        }
    }

    object CantOverWriteWorkbook{
        val header = ErrorHeader("${prefix}1", "Can't overwrite workbook in workbook state")
        fun report(detail:String? = null): ErrorReport {
            return ErrorReport(
                header = detail?.let { header.setDescription(it) } ?: header
            )
        }
    }

}
