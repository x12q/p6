package com.qxdzbc.p6.ui.workbook.state.cont

import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.SingleErrorReport

object WorkbookStateContainerErrors {
    val prefix = "WorkbookStateContainerErrors_"
    object WorkbookStateNotExist{
        val header = ErrorHeader("${prefix}0", "Workbook state does not exist")
        fun report(detail:String? = null): SingleErrorReport {
            return SingleErrorReport(
                header = detail?.let { header.setDescription(it) } ?: header
            )
        }
    }
    object WorkbookStateAlreadyExist{
        val header = ErrorHeader("${prefix}1", "Workbook state already exists")
        fun report(detail:String? = null): SingleErrorReport {
            return SingleErrorReport(
                header = detail?.let { header.setDescription(it) } ?: header
            )
        }
    }

}
