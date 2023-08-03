package com.qxdzbc.p6.ui.window.di.comp

import com.qxdzbc.p6.ui.document.workbook.di.comp.WbComponent
import com.qxdzbc.p6.ui.window.state.WindowState
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Provides
import dagger.Subcomponent
import java.util.*


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
        fun setId(@WindowIdInWindow @BindsInstance i:String):Builder
        fun build(): WindowComponent
    }

    fun windowState():WindowState
}

