package com.qxdzbc.p6.app.communication.res_req_template

import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport

object RequestErrors {
    val prefix = "RequestErrors_"
    object IllegalRequest{
        val header = ErrorHeader("${prefix}0","Illegal request")
        fun report(detail:String):ErrorReport{
            return header.setDescription(detail).toErrorReport()
        }
    }
}
