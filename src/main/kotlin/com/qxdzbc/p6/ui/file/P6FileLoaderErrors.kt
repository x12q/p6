package com.qxdzbc.p6.ui.file

import com.qxdzbc.p6.app.common.utils.path.PPath
import com.qxdzbc.p6.common.exception.error.ErrorHeader
import com.qxdzbc.p6.common.exception.error.ErrorReport

object P6FileLoaderErrors {
    val FILE_ERR ="UI_P6FileLoaderErrors_"
    val notAFileHeader = ErrorHeader("${FILE_ERR}0","path does not point to a file")
    fun notAFile(path: PPath)= ErrorReport(
        header = notAFileHeader.copy(errorDescription = "path does not point to a file: ${path}"),
        data=path,
    )
    val notReadableHeader=ErrorHeader("${FILE_ERR}1","file is not readable")
    fun notReadableFile(path: PPath) = ErrorReport(
        header = notReadableHeader.copy(errorDescription = "file ${path} is not readable"),
        data=path,
    )
}
