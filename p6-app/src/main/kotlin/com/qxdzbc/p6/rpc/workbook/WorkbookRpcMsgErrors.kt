package com.qxdzbc.p6.rpc.workbook

import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport

object WorkbookRpcMsgErrors {
    val prefix = "WorkbookRpcMsgErrors_"
    object IllegalMsg{
        val header = ErrorHeader("${prefix}0","Msg in illegal state")
        fun report(detail:String): ErrorReport {
            return header.setDescription(detail).toErrorReport()
        }
    }
}
