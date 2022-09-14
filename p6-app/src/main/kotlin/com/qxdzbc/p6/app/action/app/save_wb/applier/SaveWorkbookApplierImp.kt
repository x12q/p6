package com.qxdzbc.p6.app.action.app.save_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookResponse
import com.qxdzbc.p6.app.action.app.set_wbkey.ReplaceWorkbookKeyAction
import com.qxdzbc.p6.app.action.app.set_wbkey.SetWbKeyRequest
import com.qxdzbc.p6.app.action.applier.BaseApplier
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouters.publishErrToWindowIfNeed
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.window.state.WindowState
import java.nio.file.Path
import javax.inject.Inject

class SaveWorkbookApplierImp @Inject constructor(
    private val baseApplier: BaseApplier,
    private val errorRouter: ErrorRouter,
    @AppStateMs private val appStateMs: Ms<AppState>,
    @StateContainerMs val stateContMs: Ms<StateContainer>,
    private val replaceWbKeyAct: ReplaceWorkbookKeyAction,
) : SaveWorkbookApplier {

    private var stateCont by stateContMs
    var appState by appStateMs
    private var wbCont by stateCont.wbContMs

    override fun applyRes(res: SaveWorkbookResponse?) {
        baseApplier.applyRes(res) {
            apply(it.wbKey, it.path)
        }
    }

    fun apply(wbKey: WorkbookKey, path: String) {
        val savedPath = Path.of(path)
        if (wbKey.path != savedPath) {
//            val getWbRs = wbCont.getWbRs(wbKey)
//            getWbRs.publishErrToWindowIfNeed(errorRouter, wbKey)
            wbCont.getWbRs(wbKey)
                .publishErrToWindowIfNeed(errorRouter, wbKey)
                .onSuccess { oldWb ->
                    val newWbKey: WorkbookKey = wbKey.setPath(savedPath).setName(savedPath.fileName.toString())
                    appState.queryStateByWorkbookKey(wbKey).ifOk { qrRs ->
                        val windowState: WindowState by qrRs.windowStateMs
                        val wbState: WorkbookState by qrRs.workbookStateMs
                        val windowId = windowState.id
                        // x: update wb key in the old Workbook in wbCont
                        replaceWbKeyAct.replaceWbKey(
                            SetWbKeyRequest(
                                wbKey = wbKey,
                                newWbKey = newWbKey,
                                windowId = windowId
                            )
                        )
                            .publishErrToWindowIfNeed(errorRouter, windowId)
                            .onSuccess {
                                wbCont.removeWbRs(wbKey)
                                    .publishErrToWindowIfNeed(errorRouter, windowState.id)
                                    .onSuccess {
                                        // x: update wb key in the old WorkbookState
                                        qrRs.workbookStateMs.value = wbState
                                            .setWorkbookKeyAndRefreshState(newWbKey)
                                            .setNeedSave(false) // x: mark wb as not need save because it was already saved
                                        // x: update the active workbook pointer if need
                                        val activeWBPointerMs = windowState.activeWorkbookPointerMs
                                        if (activeWBPointerMs.value.wbKey == wbKey) {
                                            activeWBPointerMs.value = activeWBPointerMs.value.pointTo(newWbKey)
                                        }
                                    }
                                windowState.wbKeySet
                            }
                    }
                    appState.codeEditorState = appState.codeEditorState.replaceWorkbookKey(oldWb.key, newWbKey)
                }
        } else {
            stateCont.getWbStateMs(wbKey)?.also {
                it.value = it.value.setNeedSave(false)
            }
        }
    }
}
