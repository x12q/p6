package com.qxdzbc.p6.app.action.worksheet

import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplier
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class WorkbookUpdateApplierImp @Inject constructor(
    private val wbUpdateApplier: WorkbookUpdateCommonApplier
): WorkbookUpdateApplier {
    override fun applyRes(res: WorkbookUpdateCommonResponse?) {
        wbUpdateApplier.apply(res)
    }
}
