package com.emeraldblast.p6.ui.document.worksheet.cell_editor.in_cell.actions

import androidx.compose.ui.focus.FocusRequester
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.app.action.worksheet.WorksheetAction
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.actions.CellEditorActionImp
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState
import kotlin.test.BeforeTest

internal class CellEditorActionImpTest {

    lateinit var actions: CellEditorActionImp
    lateinit var stateMs: Ms<CellEditorState>
    lateinit var wsStateMs: Ms<WorksheetState>
    lateinit var wsActions: WorksheetAction
    lateinit var fc: FocusRequester

    @BeforeTest
    fun beforeTest() {
//        wsActions = mock()
//        fc = mock()
//        stateMs = ms(
//            CellEditorStateImp2(
//                cellAddress = CellAddress(1, 1),
//                currentText = "",
//                isActive = false,
//                focusRequester = fc,
//                cursorIdMs = ms(mock())
//            )
//        )
//
//        actions = CellEditorActionImp(
//            stateMs, wsActions, wsStateMs
//        )
    }

//    @Test
//    fun focus() {
//    }
//
//    @Test
//    fun enter() {
//    }
//
//    @Test
//    fun escape() {
//    }
//
//    @Test
//    fun onTextChange() {
//    }
//
//    @Test
//    fun handleSpecialKey() {
//    }
}
