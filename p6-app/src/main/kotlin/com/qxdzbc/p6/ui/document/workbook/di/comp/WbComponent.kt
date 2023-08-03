package com.qxdzbc.p6.ui.document.workbook.di.comp

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateImp
import com.qxdzbc.p6.ui.window.di.comp.WindowComponent
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Subcomponent

/**
 * A workbook component backs a single workbook and handles wiring up all the states object inside a workbook.
 * A [WbComponent] is a child of [P6Component].
 * The reason [WbComponent] is not a child of a [WindowComponent] is:
 * - a workbook can change window
 * - but a [WbComponent] cannot change the [WindowComponent] it is attached to.
 *
 * This creates an unacceptable inconsistency
 *
 */
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
        fun setWindowId(@WindowIdInWb @BindsInstance windowId:String?):Builder
        fun build(): WbComponent
    }

    @WbScope
    fun wbState(): WorkbookState

}



