package com.emeraldblast.p6.ui.action_table

import com.emeraldblast.p6.common.exception.error.ErrorHeader
import com.emeraldblast.p6.common.exception.error.ErrorReport

object WorkbookActionTableErrors {
    val prefix = "WorkbookActionTableErrors_"
    object CantGetActionForWorkbook{
        val header = ErrorHeader("${prefix}0","Can't get action obj for workbook")
        fun report(detail:String):ErrorReport{
            return header.setDescription(detail).toErrorReport()
        }
    }
}
