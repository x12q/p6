package com.qxdzbc.p6.app.action.cursor.copy_cursor_range_to_clipboard

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardRequest
import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.worksheet.WorksheetErrors
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CopyCursorRangeToClipboardActionImp @Inject constructor(
    private val wsAction: WorksheetAction,
    private val errorRouter: ErrorRouter,
    private val stateCont:StateContainer,
) : CopyCursorRangeToClipboardAction {

    private val sc = stateCont

    override fun copyCursorRangeToClipboard(wbws: WbWs) {
        val cursorState: CursorState? = sc.getCursorState(wbws)
        this.copyCursorRangeToClipboard(cursorState)
    }

    override fun copyCursorRangeToClipboard(wbwsSt: WbWsSt) {
        val cursorState: CursorState? = sc.getCursorState(wbwsSt)
        this.copyCursorRangeToClipboard(cursorState)
    }
    fun copyCursorRangeToClipboard(cursorState: CursorState?){
        if(cursorState!=null){
            val mergeAllCursorState = cursorState.attemptToMergeAllIntoOne()
            if (mergeAllCursorState.fragmentedCells.isNotEmpty() || mergeAllCursorState.fragmentedRanges.isNotEmpty()) {
                // raise error
                errorRouter.publishToWindow(WorksheetErrors.CantCopyOnFragmentedSelection, cursorState.id.wbKey)
            } else {
                val targetRange = mergeAllCursorState.mainRange
                if (targetRange != null) {
                    wsAction.rangeToClipboard(
                        RangeToClipboardRequest(
                            rangeId = RangeIdImp(
                                rangeAddress = targetRange,
                                wbKeySt = cursorState.id.wbKeySt,
                                wsNameSt = cursorState.id.wsNameSt
                            ),
                            windowId = sc.getWindowStateMsByWbKey(cursorState.id.wbKey)?.id
                        )
                    )

                } else {
                    wsAction.rangeToClipboard(
                        RangeToClipboardRequest(
                            rangeId = RangeIdImp(
                                rangeAddress = RangeAddress(cursorState.mainCell),
                                wbKeySt = cursorState.id.wbKeySt,
                                wsNameSt = cursorState.id.wsNameSt
                            ),
                            windowId = sc.getWindowStateMsByWbKey(cursorState.id.wbKey)?.id
                        )
                    )
                }
            }
        }
    }
}
