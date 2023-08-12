package com.qxdzbc.p6.composite_actions

import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport

object ActionErrors {
    private val prefix = "ActionErrors_"

    object InvalidRequest {
        val header = ErrorHeader("${com.qxdzbc.p6.composite_actions.ActionErrors.prefix}0", "Invalid request")

        fun report(detail: String?): ErrorReport {
            return (detail?.let { com.qxdzbc.p6.composite_actions.ActionErrors.InvalidRequest.header.setDescription(detail) } ?: com.qxdzbc.p6.composite_actions.ActionErrors.InvalidRequest.header).toErrorReport()
        }
    }

}
