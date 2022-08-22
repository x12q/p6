package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.common.exception.error.ErrorHeader
import com.qxdzbc.p6.common.exception.error.ErrorReport

object AppStateErrors {
    private val ASErr = "UI_AppStateErrors_"

//    object WorkbookStateNotExist{
//        val header = ErrorHeader("${ASErr}0", "Workbook state does not exist")
//        fun detail(detail:String? = null):ErrorReport{
//            return ErrorReport(
//                header = detail?.let { header.setDescription(it) } ?: header
//            )
//        }
//    }

    object InvalidWindowState {
        val header = ErrorHeader("${ASErr}1", "Invalid window")
        fun report1(workbookKey: WorkbookKey) = ErrorReport(
            header = header.setDescription("Workbook named ${workbookKey.name} at path=${workbookKey.path} is not opened by any window")
        )

        fun report2(windowId: String): ErrorReport {
            return ErrorReport(
                header = InvalidWindowState.header.setDescription("Invalid window state at id ${windowId} ")
            )
        }

        fun report3(detail: String?): ErrorReport {
            return (detail?.let {
                header.setDescription(detail)
            } ?: header).toErrorReport()
        }
    }
}
