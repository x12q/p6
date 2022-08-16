package com.emeraldblast.p6.app.file.loader

import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.common.exception.error.ErrorReport
import java.nio.file.Path
import com.github.michaelbull.result.Result

interface P6FileLoader {
    fun load(path:Path):Result<Workbook,ErrorReport>
}
