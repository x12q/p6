package com.qxdzbc.p6.app.file.saver

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto
import com.qxdzbc.p6.proto.P6FileProtos.P6FileContentProto
import com.qxdzbc.p6.proto.P6FileProtos.P6FileMetaInfoProto
import com.qxdzbc.p6.proto.P6FileProtos.P6FileProto
import com.qxdzbc.p6.ui.common.view.OkButton
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class P6SaverImp @Inject constructor() : P6Saver {
    override fun save(wb: Workbook, path: Path): Result<Unit, ErrorReport> {
        try {
            val savableWb = wb.makeSavableCopy()
            val proto: WorkbookProto = savableWb.toProto()
            val date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
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
