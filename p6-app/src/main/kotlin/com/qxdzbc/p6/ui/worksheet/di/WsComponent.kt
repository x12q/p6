package com.qxdzbc.p6.ui.worksheet.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.ui.worksheet.cursor.di.CursorModule
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Subcomponent


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


