package com.qxdzbc.p6.composite_actions.app.save_wb

import com.github.michaelbull.result.*
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.composite_actions.app.set_wbkey.ReplaceWorkbookKeyAction
import com.qxdzbc.p6.composite_actions.app.set_wbkey.SetWbKeyRequest
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.file.saver.P6Saver
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouters.publishErrToWindowIfNeed
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.file.P6FileSaverErrors
import com.squareup.anvil.annotations.ContributesBinding
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.path.absolutePathString
import kotlin.io.path.isRegularFile

@Singleton
@ContributesBinding(P6AnvilScope::class)
class SaveWorkbookActionImp @Inject constructor(
    val stateCont: StateContainer,
    val errorRouter: ErrorRouter,
    val saver: P6Saver,
    private val replaceWbKeyAct: ReplaceWorkbookKeyAction,
) : SaveWorkbookAction {

    private val sc = stateCont

    override fun saveWorkbookForRpc(
        wbKey: WorkbookKey,
        path: Path,
        windowId: String?,
        publishError: Boolean
    ): SaveWorkbookResponse {
        val rs = saveWorkbook(wbKey, path, windowId, publishError)
        return SaveWorkbookResponse(
            errorReport = if (rs is Err) rs.error else null,
            wbKey = wbKey,
            path = path.absolutePathString()
        )
    }

    override fun saveWorkbook(wbKey: WorkbookKey, path: Path, windowId: String?, publishError: Boolean): Rse<Unit> {
        val wbFromPath: Workbook? = sc.getWb(path)
        val wbAlreadyOpen = wbFromPath != null
        val pathIsMisMatch = wbKey.path != path
        if (wbAlreadyOpen && pathIsMisMatch) {
            val err = P6FileSaverErrors.TargetPathPointToAnAlreadyOpenWb.report(path)
            if (publishError) {
                errorRouter.publishToWindow(err, windowId, wbKey)
            }
            return err.toErr()
        } else {
            val res = performSave(wbKey, path)
            applySaveResult(res, publishError, wbKey, path)
            return res
        }
    }

    fun performSave(wbKey: WorkbookKey, path: Path): Rse<Unit> {
        val wbRs = sc.getWbStateRs(wbKey)
        val rt = wbRs.flatMap { wb ->
            val saveRs = if (path.isRegularFile() && path.toString().endsWith(".csv")) {
                saver.saveFirstWsAsCsv(wb.wb, path)
            } else {
                saver.saveAsProtoBuf(wb, path)
            }
            saveRs
        }
        return rt
    }

    /**
     * This function makes the assumption that if [rse] is Ok, then [wbKey] and [savedPath] are legal
     */
    fun applySaveResult(rse: Rse<Unit>, publishError: Boolean, wbKey: WorkbookKey, savedPath: Path) {
        when (rse) {
            is Ok -> {
                if (wbKey.path != savedPath) {
                    // x: wb was saved to a new path, need to update its path
                    sc.getWbStateRs(wbKey)
                        .onFailure {
                            if (publishError) {
                                errorRouter.publishToWindow(it, wbKey)
                            }
                        }
                        .onSuccess { wbStateMs ->
                            val newWbKey: WorkbookKey = wbKey.setPath(savedPath).setName(savedPath.fileName.toString())

                            sc.getWindowStateByWbKey(wbKey)?.also { windowState ->
                                val windowId = windowState.id
                                replaceWbKeyAct.replaceWbKey(SetWbKeyRequest(wbKey, newWbKey, windowId))
                                    .publishErrToWindowIfNeed(errorRouter, windowId)
                            }

                            wbStateMs.needSave = false
                        }
                } else {
                    // x: same path, no need to update wb path
                    sc.getWbState(wbKey)?.also {
                        it.needSave = false
                    }
                }
            }

            is Err -> {
                if (publishError) {
                    errorRouter.publishToWindow(rse.error, wbKey)
                }
            }
        }
    }
}
