package com.qxdzbc.p6.document_data_layer.wb_container

import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.SingleErrorReport
import java.nio.file.Path

object WorkbookContainerErrors {
    private val UI_WErr = "UI_WorkbookContainerErrors_"

    object InvalidWorkbook {
        val header = ErrorHeader("${UI_WErr}0", "invalid workbook")
        fun report(workbookKey: WorkbookKey) = SingleErrorReport(
            header = header.setDescription("Invalid workbook at ${workbookKey.path}"),
            data = workbookKey,
        )

        fun report(path: Path) = SingleErrorReport(
            header = header.setDescription("Invalid workbook at ${path}"),
            data = path
        )
        fun reportDefault(name: String) = SingleErrorReport(
            header = header.setDescription("Workbook named ${name} does not exist."),
        )
        fun report(detail: String?) = SingleErrorReport(
            header = detail?.let { header.setDescription(it) } ?: header,
        )
        fun report(index: Int) = SingleErrorReport(
            header = header.setDescription("Workbook at ${index} does not exist."),
        )
    }

    object WorkbookAlreadyExist {
        val header = ErrorHeader("${UI_WErr}1", "")
        fun report(wbKey: WorkbookKey): SingleErrorReport {
            return SingleErrorReport(
                header = header.setDescription("workbook at key ${wbKey} already exist")
            )
        }

        fun report2(detail:String?): SingleErrorReport {
            return SingleErrorReport(
                header = detail?.let { header.setDescription(detail) } ?: header
            )
        }
    }
}
