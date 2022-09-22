package com.qxdzbc.p6.app.document.cell

import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport

/**
 * This object contains ErrorReport that are only used for reporting errors encountered when running formulas in cells
 */
object FormulaErrors {
    val prefix ="ERR"

    object Unknown{
        val header=ErrorHeader(errorCode="${prefix}0","unknow error")
        fun report(detail:String):ErrorReport{
            return header.setDescription(detail).toErrorReport()
        }
    }

    object InvalidFunctionArgument{
        val header=ErrorHeader(errorCode="${prefix}1","invalid function argument")
        fun report(detail:String):ErrorReport{
            return header.setDescription(detail).toErrorReport()
        }
    }


    /**
     * Extract a representative string to show inside a cell on the UI
     */
    fun ErrorReport.getCellRepStr():String{
        return this.header.errorCode
    }

    /**
     * Extract error detail to be showed somewhere on the UI (tooltips, status bar, or similar places)
     */
    fun ErrorReport.getDetailStr():String{
        return this.header.errorDescription
    }
}
