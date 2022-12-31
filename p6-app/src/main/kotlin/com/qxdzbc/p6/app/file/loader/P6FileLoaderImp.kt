package com.qxdzbc.p6.app.file.loader

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp.Companion.toShallowModel
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.proto.P6FileProtos
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.UtilQualifier
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.proto.DocProtos

import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.squareup.anvil.annotations.ContributesBinding
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.name
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class P6FileLoaderImp @Inject constructor(
    val transContSt:St<@JvmSuppressWildcards TranslatorContainer>,
    @UtilQualifier.ReadFileFunction
    val readFileToByteArray:Function1<@JvmSuppressWildcards Path,@JvmSuppressWildcards ByteArray> = Files::readAllBytes,
) : P6FileLoader {
    private val tc by transContSt
    override fun loadToWb(path: Path): Result<Workbook, ErrorReport> {
        try {
            val bytes = readFileToByteArray(path)
            val p6File = P6FileProtos.P6FileProto.newBuilder().mergeFrom(bytes).build()
            val fileContent = P6FileProtos.P6FileContentProto.newBuilder().mergeFrom(p6File.content).build()
            val newWbKey = WorkbookKey(path.name,path)
            val loadedWbProto = fileContent.workbook.toBuilder().setWbKey(newWbKey.toProto()).build()
            val wb = loadedWbProto.toShallowModel(tc::getTranslatorOrCreate)
            return Ok(wb)
        } catch (e: Throwable) {
            return Err(CommonErrors.ExceptionError.report(e))
        }
    }

    override fun load2Rs(path: Path): Result<P6FileLoadResult, ErrorReport> {
        try {
            val bytes = readFileToByteArray(path)
            val p6File = P6FileProtos.P6FileProto.newBuilder().mergeFrom(bytes).build()
            val fileContent = P6FileProtos.P6FileContentProto.newBuilder().mergeFrom(p6File.content).build()
            val newWbKey = WorkbookKey(path.name,path)
            val loadedWbProto = fileContent.workbook.toBuilder().setWbKey(newWbKey.toProto()).build()
            val wb = loadedWbProto.toShallowModel(tc::getTranslatorOrCreate)
            val loadResult = P6FileLoadResult(
                wb,loadedWbProto
            )
            return Ok(loadResult)
        } catch (e: Throwable) {
            return Err(CommonErrors.ExceptionError.report(e))
        }
    }

    override fun load3Rs(path: Path): Result<DocProtos.WorkbookProto, ErrorReport> {
        try {
            val bytes = readFileToByteArray(path)
            val p6File = P6FileProtos.P6FileProto.newBuilder().mergeFrom(bytes).build()
            val fileContent = P6FileProtos.P6FileContentProto.newBuilder().mergeFrom(p6File.content).build()
            val newWbKey = WorkbookKey(path.name,path)
            val loadedWbProto = fileContent.workbook.toBuilder().setWbKey(newWbKey.toProto()).build()
            return Ok(loadedWbProto)
        } catch (e: Throwable) {
            return Err(CommonErrors.ExceptionError.report(e))
        }
    }
}
