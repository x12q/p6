package com.qxdzbc.p6.app.action.worksheet.rename_ws.applier

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.applier.BaseApplier
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse
import com.github.michaelbull.result.map
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RenameWorksheetApplierImp @Inject constructor(
    val i: RenameWorksheetInternalApplier,
    private val baseApplier: BaseApplier
) : RenameWorksheetApplier {

    override fun applyResRs(res: RenameWorksheetResponse?): Rse<Unit> {
        return baseApplier.applyResRs(res).map {
            i.apply(it.wbKey,it.oldName,it.newName)
        }
    }
}
