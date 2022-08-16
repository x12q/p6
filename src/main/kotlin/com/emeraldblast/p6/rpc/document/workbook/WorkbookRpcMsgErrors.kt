package com.emeraldblast.p6.rpc.document.workbook

import com.emeraldblast.p6.common.exception.error.ErrorHeader
import com.emeraldblast.p6.common.exception.error.ErrorReport

object WorkbookRpcMsgErrors {
    val prefix = "WorkbookRpcMsgErrors_"
    object IllegalMsg{
        val header = ErrorHeader("${prefix}0","Msg in illegal state")
        fun report(detail:String):ErrorReport{
            return header.setDescription(detail).toErrorReport()
        }
    }
}
