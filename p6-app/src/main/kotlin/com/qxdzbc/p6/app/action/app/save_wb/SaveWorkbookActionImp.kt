package com.qxdzbc.p6.app.action.app.save_wb

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.app.save_wb.applier.SaveWorkbookApplier
import com.qxdzbc.p6.app.action.app.save_wb.rm.SaveWorkbookRM
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.ui.app.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.file.P6FileSaverErrors
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.absolutePathString

class SaveWorkbookActionImp @Inject constructor(
    @StateContainerSt
    val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    val errorRouter: ErrorRouter,
    val rm: SaveWorkbookRM,
    val applier: SaveWorkbookApplier,
) : SaveWorkbookAction {
    private val sc by stateContSt
    override fun saveWorkbook(wbKey: WorkbookKey, path: Path, windowId: String?): SaveWorkbookResponse {
        val wb: Workbook? = sc.getWb(path)
        val wbAlreadyOpen = wb != null
        val targetWbIsDifferentFromTheWBInPath = wbKey.path != path
        if (wbAlreadyOpen && targetWbIsDifferentFromTheWBInPath) {
            val err = P6FileSaverErrors.WorkbookIsAlreadyOpenForEditing(path)
            errorRouter.publishToWindow(err, windowId, wbKey)
            return SaveWorkbookResponse(
                errorReport = err,
                wbKey = wbKey,
                path = path.absolutePathString()
            )
        } else {
            val request = SaveWorkbookRequest(
                wbKey = wbKey,
                path = path.toAbsolutePath().toString(),
            )
            val res = rm.saveWb(request)
            applier.applyRes(res)
            return res
        }
    }
}
