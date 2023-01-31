package com.qxdzbc.p6.app.action.app


import com.qxdzbc.p6.app.action.workbook.set_active_ws.rm.SetActiveWorksheetRM
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class,boundType=AppRM::class)
class AppRMImp @Inject constructor(
    private val setActiveWsRM: SetActiveWorksheetRM,
) : AppRM, SetActiveWorksheetRM by setActiveWsRM{

}
