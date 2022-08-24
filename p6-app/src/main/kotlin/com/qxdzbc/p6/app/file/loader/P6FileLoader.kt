package com.qxdzbc.p6.app.file.loader

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.common.error.ErrorReport
import java.nio.file.Path
import com.github.michaelbull.result.Result

interface P6FileLoader {
    fun load(path:Path):Result<Workbook,ErrorReport>
}
