package com.qxdzbc.p6.ui.worksheet.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.ui.worksheet.cursor.di.CursorModule
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Subcomponent

/**
 * This component is tied to [WorksheetState]. Whenever a new [WorksheetState] is created, a new [WsComponent] is created along with it. [WorksheetState] self is not stored inside this component, but it can be provided by this component.
 */
@WsScope
@MergeSubcomponent(
    scope = WsAnvilScope::class,
    modules = [
        WsModule::class,
        CursorModule::class,
    ]
)
interface WsComponent {
    @Subcomponent.Builder
    interface Builder {
        fun setWs(@BindsInstance wsMs: Ms<Worksheet>): Builder
        fun build(): WsComponent
    }

    fun wsState(): WorksheetState
}


