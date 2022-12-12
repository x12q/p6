package com.qxdzbc.p6.app.action.worksheet.delete_cell.applier

import com.qxdzbc.p6.app.action.worksheet.delete_cell.DeleteCellResponse
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class DeleteCellResponseApplierImp @Inject constructor(
    private val i: DeleteCellResponseInternalApplier,
    private val errorRouter: ErrorRouter,
) : DeleteCellResponseApplier {
    override fun applyRes(res: DeleteCellResponse?) {
        if (res != null) {
            val err = res.errorReport
            if (err != null) {
                errorRouter.publishToWindow(err, res.wbKey)
            } else {
                i.apply(res.wbKey, res.wsName, res.cellAddress, res.newWorkbook)
            }
        }
    }
}
