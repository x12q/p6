package com.qxdzbc.p6.app.document.wb_container

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.common.exception.error.ErrorHeader
import com.qxdzbc.p6.common.exception.error.ErrorReport
import java.nio.file.Path

object WorkbookContainerErrors {
    private val UI_WErr = "UI_WorkbookContainerErrors_"

    object InvalidWorkbook {
        val header = ErrorHeader("${UI_WErr}0", "invalid workbook")
        fun report(workbookKey: WorkbookKey) = ErrorReport(
            header = header.setDescription("Invalid workbook at ${workbookKey.path}"),
            data = workbookKey,
        )

        fun report(path: Path) = ErrorReport(
            header = header.setDescription("Invalid workbook at ${path}"),
            data = path
        )
        fun reportDefault(name: String) = ErrorReport(
            header = header.setDescription("Workbook named ${name} does not exist."),
        )
        fun report(detail: String?) = ErrorReport(
            header = detail?.let { header.setDescription(it) } ?: header,
        )
        fun report(index: Int) = ErrorReport(
            header = header.setDescription("Workbook at ${index} does not exist."),
        )
    }

    object WorkbookAlreadyExist {
        val header = ErrorHeader("${UI_WErr}1", "")
        fun report(wbKey: WorkbookKey): ErrorReport {
            return ErrorReport(
                header = header.setDescription("workbook at key ${wbKey} already exist")
            )
        }
    }
}
