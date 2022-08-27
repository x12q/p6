package com.qxdzbc.p6.app.action.range.paste_range.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.command.Commands
import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplier
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeResponse
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import javax.inject.Inject

class PasteRangeApplierImp @Inject constructor(
    private val wbUpdateApplier: WorkbookUpdateCommonApplier,
    @AppStateMs private val appStateMs:Ms<AppState>,
) : PasteRangeApplier {
    private var appState by appStateMs
    override fun applyPasteRange(res: PasteRangeResponse?) {
        if(res!=null){
            val wbKey = res.wbKey
            val reverseRes = WorkbookUpdateCommonResponse(
                errorReport = null,
                wbKey = wbKey,
                newWorkbook = wbKey?.let{appState.getWb(it)},
                windowId = res.windowId
            )

            val command = Commands.makeCommand(
                run = {wbUpdateApplier.apply(res)},
                undo = {wbUpdateApplier.apply(reverseRes)}
            )
            if(wbKey!=null){
                val commandStackMs=appState.getWbState(wbKey)?.commandStackMs
                if(commandStackMs!=null){
                    commandStackMs.value = commandStackMs.value.add(command)
                }
            }
            command.run()
        }
    }
}
