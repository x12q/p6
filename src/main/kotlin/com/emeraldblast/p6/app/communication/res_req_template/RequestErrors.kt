package com.emeraldblast.p6.app.communication.res_req_template

import com.emeraldblast.p6.common.exception.error.ErrorHeader
import com.emeraldblast.p6.common.exception.error.ErrorReport

object RequestErrors {
    val prefix = "RequestErrors_"
    object IllegalRequest{
        val header = ErrorHeader("${prefix}0","Illegal request")
        fun report(detail:String):ErrorReport{
            return header.setDescription(detail).toErrorReport()
        }
    }
}
