package com.qxdzbc.p6.ui.document.workbook.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.document.workbook.di.comp.WbComponent
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
    fun makeWbState(wbMs: Ms<Workbook>, windowId: String?): WorkbookState {
        val rt = wbCompBuilderProvider
            .get()
            .setWb(wbMs)
            .setWindowId(windowId)
            .build()
            .wbState()
        return rt
    }

    override fun createAndRefresh(
        wbMs: Ms<Workbook>,
        windowId: String?,
    ): WorkbookState {
        return makeWbState(
            wbMs, windowId
        ).apply {
            refresh()
        }
    }

    override fun create(wbMs: Ms<Workbook>): WorkbookState {
        return this.createAndRefresh(wbMs,null)
    }
}