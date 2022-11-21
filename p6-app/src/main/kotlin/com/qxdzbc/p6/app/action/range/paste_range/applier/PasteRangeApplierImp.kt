package com.qxdzbc.p6.app.action.range.paste_range.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.command.Commands
import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplier
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeResponse

import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class PasteRangeApplierImp @Inject constructor(
    private val wbUpdateApplier: WorkbookUpdateCommonApplier,
    val stateContMs: Ms<SubAppStateContainer>,
    val docContMs:Ms<DocumentContainer>,
) : PasteRangeApplier {

    private var sc by stateContMs
    private var dc by docContMs

    override fun applyPasteRange(res: PasteRangeResponse?) {
        if(res!=null){
            val wbKey = res.wbKey
            val reverseRes = WorkbookUpdateCommonResponse(
                errorReport = null,
                wbKey = wbKey,
                newWorkbook = wbKey?.let{dc.getWb(it)},
                windowId = res.windowId
            )

            val command = Commands.makeCommand(
                run = {wbUpdateApplier.apply(res)},
                undo = {wbUpdateApplier.apply(reverseRes)}
            )
            if(wbKey!=null){
                val commandStackMs=sc.getWbState(wbKey)?.commandStackMs
                if(commandStackMs!=null){
                    commandStackMs.value = commandStackMs.value.add(command)
                }
            }
            command.run()
        }
    }
}
