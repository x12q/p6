package com.qxdzbc.p6.app.file.loader

import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto
import java.nio.file.Path

interface P6FileLoader {
    fun loadToWb(path:Path):Result<Workbook,ErrorReport>
    fun load2Rs(path:Path):Result<P6FileLoadResult,ErrorReport>
    fun load3Rs(path:Path):Result<WorkbookProto,ErrorReport>
}
