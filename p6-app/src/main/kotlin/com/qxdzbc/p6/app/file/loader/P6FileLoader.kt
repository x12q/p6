package com.qxdzbc.p6.app.file.loader

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto
import java.nio.file.Path

interface P6FileLoader {
    fun loadToWb(path: Path): Rse<Workbook>
    fun load2Rs(path: Path): Rse<P6FileLoadResult>
    fun load3Rs(path: Path): Rse<WorkbookProto>
}
