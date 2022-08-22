package com.qxdzbc.p6.app.file.saver

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result
import java.nio.file.Path

interface P6Saver {
    fun save(wb:Workbook,path:Path): Result<Unit, ErrorReport>
}