package com.qxdzbc.p6.composite_actions.app.set_active_wb


import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class SetActiveWorkbookActionImp @Inject constructor(
    val stateCont:StateContainer,
    val activeWindowPointer:ActiveWindowPointer,
) : SetActiveWorkbookAction {

    override fun setActiveWb(wbk: WorkbookKey): Rse<Unit> {
        val windowStateRs = stateCont.getWindowStateByWbKeyRs(wbk)
        val rt= windowStateRs.map {owds->
            val wds = owds
            activeWindowPointer.pointTo(wds.id)
            stateCont.getWbKeyMs(wbk)?.also {
                wds.activeWbPointerMs.value = wds.activeWbPointer.pointTo(it)
            }
            Unit
        }
        return rt
    }
}
