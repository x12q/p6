package com.qxdzbc.p6.app.action.workbook.delete_worksheet.applier

import com.github.michaelbull.result.flatMap
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.Rse

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class DeleteWorksheetApplierImp @Inject constructor(
    private val docCont: DocumentContainer,
    private val subAppStateContainer: StateContainer,
    val transContMs:TranslatorContainer,
) : DeleteWorksheetApplier {

    private var tc = transContMs

    override fun applyResRs(deletedWsName:String,rs: Rse<Workbook>): Rse<Unit> {
        return rs.flatMap { newWB->
            // x: update wb
            docCont.replaceWb(newWB)
            // x: update wb state
            val wbKey = newWB.key
            val wbStateMs = subAppStateContainer.getWbStateMs(wbKey)
            wbStateMs?.value?.refresh()
            wbStateMs?.value?.needSave = true
            //update translator map
            tc.removeTranslator(wbKey,deletedWsName)
            Unit.toOk()
        }
    }
}
