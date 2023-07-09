package com.qxdzbc.p6.app.action.applier.common

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import javax.inject.Inject

class UpdateWorkbookStateApplierImp @Inject constructor(
    val stateCont:StateContainer,
) : UpdateWorkbookStateApplier {
   
    override fun updateWbState(newWbState: WorkbookState?) {
        if(newWbState!=null){
            stateCont.wbStateCont.updateWbState(newWbState)
        }
    }
}
