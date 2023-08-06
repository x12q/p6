package com.qxdzbc.p6.ui.document.worksheet.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.document.worksheet.di.comp.WsComponent
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton


@Singleton
@ContributesBinding(P6AnvilScope::class)
class WorksheetStateFactoryImp @Inject constructor(
    val componentBuilderProvider: Provider<WsComponent.Builder>
) : WorksheetStateFactory {
    override fun create(wsMs: Ms<Worksheet>): WorksheetState {
        val compBuilder = componentBuilderProvider.get()
        val comp = compBuilder
            .setWs(wsMs)
            .build()
        val rt = comp.wsState()
        rt.refresh()
        return rt
    }
}
