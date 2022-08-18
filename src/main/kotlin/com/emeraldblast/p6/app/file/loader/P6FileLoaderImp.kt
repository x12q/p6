package com.emeraldblast.p6.app.file.loader

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookImp.Companion.toModel
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.P6FileProtos
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.name

class P6FileLoaderImp @Inject constructor(
    @AppStateMs appStateMs:Ms<AppState>,
) : P6FileLoader {
    private var appState by appStateMs
    override fun load(path: Path): Result<Workbook, ErrorReport> {
        try {
            val bytes = Files.readAllBytes(path)
            val p6File = P6FileProtos.P6FileProto.newBuilder().mergeFrom(bytes).build()
            val fileContent = P6FileProtos.P6FileContentProto.newBuilder().mergeFrom(p6File.content).build()
            val newWbKey = WorkbookKey(path.name,path)
            val newProto = fileContent.workbook.toBuilder().setWorkbookKey(newWbKey.toProto()).build()
            val wb = newProto.toModel(appState.translatorContainer::getTranslator)
            return Ok(wb)
        } catch (e: Throwable) {
            return Err(CommonErrors.ExceptionError.report(e))
        }
    }
}
