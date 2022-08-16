package com.emeraldblast.p6.ui.document.workbook.state.cont

import com.emeraldblast.p6.common.exception.error.ErrorHeader
import com.emeraldblast.p6.common.exception.error.ErrorReport

object WorkbookStateContainerErrors {
    val prefix = "WorkbookStateContainerErrors_"
    object WorkbookStateNotExist{
        val header = ErrorHeader("${prefix}0", "Workbook state does not exist")
        fun report(detail:String? = null): ErrorReport {
            return ErrorReport(
                header = detail?.let { header.setDescription(it) } ?: header
            )
        }
    }
    object WorkbookStateAlreadyExist{
        val header = ErrorHeader("${prefix}1", "Workbook state already exists")
        fun report(detail:String? = null): ErrorReport {
            return ErrorReport(
                header = detail?.let { header.setDescription(it) } ?: header
            )
        }
    }

}
