package com.qxdzbc.p6.ui.document.workbook

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.qxdzbc.p6.app.common.utils.WorkbookUtils
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetRequest
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetRequest
import com.qxdzbc.p6.ui.document.workbook.action.WorkbookActionTable
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.view.dialog.Dialogs
import com.qxdzbc.p6.app.action.workbook.WorkbookAction
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt
import com.qxdzbc.p6.ui.theme.language.P6UIText
import com.qxdzbc.p6.ui.document.workbook.dialog.DeleteWorksheetDialog
import com.qxdzbc.p6.ui.document.workbook.sheet_tab.bar.SheetTabBarView
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.worksheet.WorksheetView
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.theme.language.AppLanguage
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState


@Composable
fun WorkbookView(
    wbState: WorkbookState,
    wbActionTable: WorkbookActionTable,
    focusState: WindowFocusState
) {
    val wbAction: WorkbookAction = wbActionTable.wbAction
    val wb = wbState.wb
    val wbKey = wb.key
    MBox(modifier = Modifier.fillMaxSize()) {
        var openRenameDialog by rms(false)
        var openDeleteDialog by rms(false)
        var renameTarget: String by rms("")
        var newSheetName: String by rms("")
        var deleteTarget: String by rms("")
        Column(modifier = Modifier.fillMaxSize()) {
            MBox(modifier = Modifier.fillMaxSize().weight(1.0F)) {
                val wsMs: WorksheetState? = wbState.activeSheetStateMs
                if (wsMs != null) {
                    WorksheetView(
                        wsState = wsMs,
                        worksheetActionTable = wbActionTable.worksheetActionTable,
                        focusState = focusState
                    )
                }
            }
            BorderBox(style = BorderStyle.TOP, modifier = Modifier.height(30.dp)) {
                SheetTabBarView(
                    state = wbState.sheetTabBarState,
                    onItemClick = { sheetName ->
                        if (sheetName != wbState.activeSheetPointer.wsName) {
                            val request = SetActiveWorksheetRequest(
                                wbKey = wbKey,
                                wsName = sheetName,
                            )
//                            wbAction.setFocusConsideringRangeSelector(wbKey)
//                            wbAction.setActiveWs(request)
                            wbAction.switchToWorksheet(request)
                        }
                    }, onNewButtonClick = {
                        newSheetName = WorkbookUtils.generateNewSheetName(wb.worksheets.map { it.name })
                        wbAction.createNewWorksheetRs(
                            CreateNewWorksheetRequest(
                                wbKey = wb.key,
                                newWorksheetName = newSheetName
                            )
                        )
                    }, onRename = { targetName ->
                        openRenameDialog = true
                        renameTarget = targetName
                    }, onDelete = { targetName ->
                        openDeleteDialog = true
                        deleteTarget = targetName
                    })
            }

            if (openRenameDialog) {
                Dialogs.RenameDialog(
                    title = AppLanguage.uiText.renameSheetDialogTitle,
                    initName = renameTarget,
                    onOk = { newName ->
                        wbAction.renameWorksheetRs(
                            RenameWorksheetRequest(
                                wb.key,
                                renameTarget,
                                newName
                            ),true
                        )
                        openRenameDialog = false
                    }, onCancel = {
                        openRenameDialog = false
                    })
            }
            if (openDeleteDialog) {
                DeleteWorksheetDialog(
                    sheetName = deleteTarget,
                    onOk = {
                        wbAction.deleteWorksheetRs(
                            request= WorksheetIdWithIndexPrt(
                                wbKey = wb.key,
                                wsName =deleteTarget,
                                wsIndex = null
                            )
                        )
                        openDeleteDialog = false
                    },
                    onCancel = {
                        openDeleteDialog = false
                    }
                )
            }
        }
    }
}
