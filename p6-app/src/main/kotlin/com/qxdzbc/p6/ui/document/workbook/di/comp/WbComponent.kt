package com.qxdzbc.p6.ui.document.workbook.di.comp

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.worksheet.di.comp.WsComponent
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Subcomponent

@WbScope
@MergeSubcomponent(
    scope = WbAnvilScope::class,
    modules = [
        ModuleForSubComponentsForWbComponent::class,
        WbModule::class,
    ]
)
interface WbComponent {

    @Subcomponent.Builder
    interface Builder {
        fun setWb(@BindsInstance wbMs: Ms<Workbook>): Builder
        fun build(): WbComponent
    }

    fun wbState(): WorkbookState

    fun wsCompBuilder(): WsComponent.Builder
}

