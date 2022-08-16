package com.emeraldblast.p6.ui.file

import com.emeraldblast.p6.common.exception.error.ErrorHeader
import com.emeraldblast.p6.common.exception.error.ErrorReport
import java.nio.file.Path

object P6FileSaverErrors {
    val prefix = "UI_P6FileSaverErrors_"
    fun WorkbookIsAlreadyOpenForEditing(path:Path):ErrorReport{
        return ErrorHeader("${prefix}_0","workbook at path ${path.toAbsolutePath().toString()} is already opened for editing, another workbook cannot overwrite it").toErrorReport()
    }
}
