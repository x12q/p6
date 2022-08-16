package com.emeraldblast.p6.app.action.worksheet.rename_ws.applier

import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.action.applier.BaseApplier
import com.emeraldblast.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse
import com.github.michaelbull.result.map
import javax.inject.Inject

class RenameWorksheetApplierImp @Inject constructor(
    val i: RenameWorksheetInternalApplier,
    private val baseApplier: BaseApplier
) : RenameWorksheetApplier {

    override fun applyResRs(res: RenameWorksheetResponse?):Rse<Unit> {
        return baseApplier.applyResRs(res).map {
            i.apply(it.wbKey,it.oldName,it.newName)
        }
    }
}
