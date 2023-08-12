package com.qxdzbc.p6.app_context

import com.github.michaelbull.result.Err
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.common.error.ErrorReport

/**
 * Errors when starting the app
 */
object InitErrors {
    private val label = "UI_InitErrors_"
    fun invalidInitScript() = SingleErrorReport(
        header = ErrorHeader("${label}0", "missing/invalid init script")
    )

    object UnableToStartKernel{
        val header = ErrorHeader("${label}1", "Unable to start python kernel")
        fun report():ErrorReport{
            return header.toErrorReport()
        }
        fun err():Err<ErrorReport>{
            return report().toErr()
        }
    }

}
