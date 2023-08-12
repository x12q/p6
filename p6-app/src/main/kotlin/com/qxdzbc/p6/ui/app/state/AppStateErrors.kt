package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

object AppStateErrors {
    private val ASErr = "UI_AppStateErrors_"

    object NoActiveWorkbook {
        val header = ErrorHeader("${ASErr}2", "No active workbook")
        fun report(detail: String?=null): ErrorReport {
            return (detail?.let { header.setDescription(detail) } ?: header).toErrorReport()
        }
    }

    object InvalidWindowState {
        val header = ErrorHeader("${ASErr}1", "Invalid window")
        fun report1(workbookKey: WorkbookKey) = SingleErrorReport(
            header = header.setDescription("Workbook named ${workbookKey.name} at path=${workbookKey.path} is not opened by any window")
        )

        fun report2(windowId: String): SingleErrorReport {
            return SingleErrorReport(
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
