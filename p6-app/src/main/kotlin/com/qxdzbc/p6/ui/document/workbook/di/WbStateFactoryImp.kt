package com.qxdzbc.p6.ui.document.workbook.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.document.workbook.di.comp.WbComponent
import com.qxdzbc.p6.ui.document.workbook.di.comp.WbComponent.Companion.wbStateWithComp
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@ContributesBinding(scope = P6AnvilScope::class, boundType = WbStateFactory::class)
class WbStateFactoryImp @Inject constructor(
    val wbCompBuilderProvider: Provider<WbComponent.Builder>,
) : WbStateFactory {

    /**
     * whenever [wbCompBuilderProvider] is called. It creates a new [WbComponent.Builder], therefore, a new [WbComponent] is created for each new [WorkbookState].
     */
    override fun makeWbState(wbMs: Ms<Workbook>): WorkbookState {
        val rt = wbCompBuilderProvider
            .get()
            .setWb(wbMs)
            .build()
            .wbStateWithComp()
        return rt
    }

}