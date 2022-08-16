package com.emeraldblast.p6.app.file.saver

import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.DocProtos.WorkbookProto
import com.emeraldblast.p6.proto.P6FileProtos.P6FileContentProto
import com.emeraldblast.p6.proto.P6FileProtos.P6FileMetaInfoProto
import com.emeraldblast.p6.proto.P6FileProtos.P6FileProto
import com.emeraldblast.p6.ui.common.view.OkButton
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject

class P6SaverImp @Inject constructor() : P6Saver {
    override fun save(wb: Workbook, path: Path): Result<Unit, ErrorReport> {
        try {
            val savableWb = wb.makeSavableCopy()
            val proto: WorkbookProto = savableWb.toProto()
            val date = 1.0F // TODO add date
            val fileContent = P6FileContentProto.newBuilder()
                .setMeta(
                    P6FileMetaInfoProto.newBuilder()
                        .setDate(date).build()
                ).setWorkbook(proto)
                .build()
            val fileProto = P6FileProto.newBuilder()
                .setVersion("1")
                .setContent(fileContent.toByteString())
                .build()
            Files.write(path, fileProto.toByteString().toByteArray())
            return Ok(Unit)
        } catch (e: Throwable) {
            return Err(CommonErrors.ExceptionError.report(e))
        }
    }
}
