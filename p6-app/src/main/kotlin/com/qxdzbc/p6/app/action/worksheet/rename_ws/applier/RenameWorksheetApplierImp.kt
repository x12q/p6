package com.qxdzbc.p6.app.action.worksheet.rename_ws.applier

import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse
import com.github.michaelbull.result.map
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RenameWorksheetApplierImp @Inject constructor(
    val ia: RenameWorksheetInternalApplier,
    val errorRouter:ErrorRouter
) : RenameWorksheetApplier {

    override fun applyResRs(res: RenameWorksheetResponse?): Rse<Unit> {
        if(res!=null){
            val err = res.errorReport
            if(err!=null){
                errorRouter.publishToWindow(err,res.wbKey)
                return err.toErr()
            }else{
                ia.apply(res.wbKey,res.oldName,res.newName)
                return Ok(Unit)
            }
        }else{
            return Ok(Unit)
        }
    }
}
