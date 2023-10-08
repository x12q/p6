package com.qxdzbc.p6.ui.worksheet.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.ui.worksheet.action.WorksheetLocalActions
import com.qxdzbc.p6.ui.worksheet.cursor.di.CursorModule
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.worksheet.state.WorksheetStateFactory
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Subcomponent

/**
 * This component is tied to [WorksheetState]. Whenever a new [WorksheetState] is created, a new [WsComponent] is created along with it.
 *
 * This Component however, should NOT be created manually even though a builder for it exists and can be used perfectly fine. This component is created indirectly along a [WorksheetState] by [WorksheetStateFactory]. The use of [WorksheetStateFactory] ensure that this component will be cleared along its [WorksheetState] when the state is removed.
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

    val localAction: WorksheetLocalActions
    fun wsState(): WorksheetState

    @Subcomponent.Builder
    interface Builder {
        fun setWs(@BindsInstance wsMs: Ms<Worksheet>): Builder
        fun build(): WsComponent
    }
}


