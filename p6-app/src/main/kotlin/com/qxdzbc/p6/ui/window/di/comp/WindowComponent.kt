package com.qxdzbc.p6.ui.window.di.comp

import com.qxdzbc.p6.ui.window.di.qualifiers.WindowIdInWindow
import com.qxdzbc.p6.ui.window.state.WindowState
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
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
        fun setId(@WindowIdInWindow @BindsInstance i:String):Builder
        fun build(): WindowComponent
    }

    fun windowState():WindowState
}

