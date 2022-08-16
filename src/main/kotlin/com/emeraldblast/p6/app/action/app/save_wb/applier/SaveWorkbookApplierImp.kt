package com.emeraldblast.p6.app.action.app.save_wb.applier

import com.emeraldblast.p6.app.action.applier.BaseApplier
import com.emeraldblast.p6.app.action.app.save_wb.SaveWorkbookResponse
import javax.inject.Inject

class SaveWorkbookApplierImp @Inject constructor(
    val internalApplier: SaveWorkbookInternalApplier,
    private val baseApplier: BaseApplier
) : SaveWorkbookApplier {
    override fun applyRes(res: SaveWorkbookResponse?) {
        baseApplier.applyRes(res){
            internalApplier.apply(it.wbKey,it.path)
        }
    }
}
