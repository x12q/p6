package com.qxdzbc.p6.ui.window.di.comp

import com.qxdzbc.p6.ui.document.workbook.di.comp.WbComponent
import com.qxdzbc.p6.ui.window.state.WindowState
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.Subcomponent


@WindowScope
@MergeSubcomponent(
    scope = WindowAnvilScope::class,
    modules = [
        ModuleForSubComponentsForWindowComponent::class,
        WindowModule::class,
    ]
)
interface WindowComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): WindowComponent
    }


    fun wbCompBuilder(): WbComponent.Builder
    fun windowState():WindowState
}

