package com.qxdzbc.p6.ui.file

import com.qxdzbc.common.path.PPath
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.SingleErrorReport

object P6FileLoaderErrors {
    val FILE_ERR ="UI_P6FileLoaderErrors_"
    val notAFileHeader = ErrorHeader("${FILE_ERR}0","path does not point to a valid file")
    fun notAFile(path: PPath)= SingleErrorReport(
        header = notAFileHeader.copy(errorDescription = "path \"${path}\" does not point to a valid file"),
        data=path,
    )
    val notReadableHeader=ErrorHeader("${FILE_ERR}1","file is not readable")
    fun notReadableFile(path: PPath) = SingleErrorReport(
        header = notReadableHeader.copy(errorDescription = "file ${path} is not readable"),
        data=path,
    )
}
