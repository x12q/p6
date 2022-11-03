package com.qxdzbc.p6.app.action.app.load_wb.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookRequest
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookResponse

import com.qxdzbc.p6.app.file.loader.P6FileLoader
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlin.io.path.Path
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class LoadWorkbookRMImp @Inject constructor(
    private val loader: P6FileLoader,
) : LoadWorkbookRM {
    override fun loadWb(request: LoadWorkbookRequest): LoadWorkbookResponse {
        val loadRs = loader.load(request.path.path)
        when (loadRs) {
            is Ok -> {
                return LoadWorkbookResponse(
                    windowId = request.windowId,
                    errorReport = null,
                    workbook = loadRs.value
                )
            }
            is Err -> {
                return LoadWorkbookResponse(
                    windowId = request.windowId,
                    errorReport = loadRs.error,
                    workbook = null
                )
            }
        }
    }
}
