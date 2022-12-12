package com.qxdzbc.p6.app.action.app.load_wb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.path.PPath
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookApplier
import com.qxdzbc.p6.app.action.app.load_wb.rm.LoadWorkbookRM
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.file.P6FileLoaderErrors
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class LoadWorkbookActionImp @Inject constructor(
    val stateContMs: Ms<StateContainer>,
    val errorRouter: ErrorRouter,
    val rm: LoadWorkbookRM,
    val applier: LoadWorkbookApplier,
) : LoadWorkbookAction {
    private var sc by stateContMs
    override fun loadWorkbook(request: LoadWorkbookRequest): LoadWorkbookResponse {
        val path: PPath = request.path
        val windowId: String? = request.windowId
        val windowStateMs = sc.getWindowStateMsDefaultRs(windowId)
        if (path.exists() && path.isRegularFile()) {
            if (path.isReadable()) {
                val res = rm.loadWb(request)
                applier.applyRes(res)
                return res
            } else {
                val e = P6FileLoaderErrors.notReadableFile(path)
                if (windowStateMs is Ok) {
                    windowStateMs.value.value.publishError(e)
                } else {
                    errorRouter.publishToApp(e)
                }
                return LoadWorkbookResponse(
                    errorReport = e,
                    windowId = request.windowId,
                    wb = null
                )
            }
        } else {
            val e = P6FileLoaderErrors.notAFile(path)
            if (windowStateMs is Ok) {
                windowStateMs.value.value.publishError(e)
            } else {
                errorRouter.publishToApp(e)
            }
            return LoadWorkbookResponse(
                errorReport = e,
                windowId = request.windowId,
                wb = null
            )
        }
    }
}
