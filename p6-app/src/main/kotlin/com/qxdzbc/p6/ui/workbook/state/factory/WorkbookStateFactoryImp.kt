package com.qxdzbc.p6.ui.workbook.state.factory

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.workbook.di.comp.WbComponent
import com.qxdzbc.p6.ui.workbook.state.WorkbookState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class WorkbookStateFactoryImp @Inject constructor(
    val wbCompBuilderProvider: Provider<WbComponent.Builder>,
) : WorkbookStateFactory {

    /**
     * whenever [wbCompBuilderProvider] is called. It creates a new [WbComponent.Builder], therefore, a new [WbComponent] is created for each new [WorkbookState].
     */
    override fun create(
        wbMs: Ms<Workbook>,
        windowId: String?,
    ): WorkbookState {
        val rt = wbCompBuilderProvider
            .get()
            .setWb(wbMs)
            .setWindowId(windowId)
            .build()
            .wbState()
            .apply {
                // refresh this state immediately
                refresh()
            }
        return rt
    }
}