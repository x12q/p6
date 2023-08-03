package com.qxdzbc.p6.ui.document.workbook.di.comp

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.P6Component
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateImp
import com.qxdzbc.p6.ui.document.worksheet.di.comp.WsComponent
import com.qxdzbc.p6.ui.window.di.comp.WindowComponent
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Subcomponent

/**
 * A workbook component backs a single workbook and handles wiring up all the states object inside a workbook.
 * A [WbComponent] is a child of [WindowComponent]
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
        fun setWindowId(@WindowId @BindsInstance windowId:String?):Builder
        fun build(): WbComponent
    }

    fun wbState(): WorkbookState

    companion object{
        /**
         * Create a new [WorkbookState], and make it hold a ref to the [WbComponent] that created it.
         */
        fun WbComponent.wbStateWithComp():WorkbookState{
            return (wbState() as WorkbookStateImp).apply{
                comp = this@wbStateWithComp
            }
        }
    }
}



