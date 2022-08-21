package com.emeraldblast.p6.app.app_context

import com.emeraldblast.p6.common.exception.error.ErrorHeader
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Err

/**
 * Errors when starting the app
 */
object InitErrors {
    private val label = "UI_InitErrors_"
    fun invalidInitScript() = ErrorReport(
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
