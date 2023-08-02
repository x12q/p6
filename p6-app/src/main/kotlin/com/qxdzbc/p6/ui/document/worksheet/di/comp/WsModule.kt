package com.qxdzbc.p6.ui.document.worksheet.di.comp

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateFactory
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbStateFactory
import com.qxdzbc.p6.ui.document.worksheet.slider.LimitedGridSliderFactory
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateFactory
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateFactory.Companion.createThenRefresh
import dagger.Module
import dagger.Provides

@Module
interface WsModule {
    companion object {
        @Provides
        @WsScope
        fun wsState(
            wsStateFactory: WorksheetStateFactory,
            gridSliderFactory: LimitedGridSliderFactory,
            cursorStateFactory: CursorStateFactory,
            thumbStateFactory: ThumbStateFactory,
            wsMs: Ms<Worksheet>
        ): WorksheetState {

            val wsState = wsStateFactory.createThenRefresh(
                wsMs = wsMs,
                gridSliderFactory = gridSliderFactory,
                cursorStateFactory = cursorStateFactory,
                thumbStateFactory = thumbStateFactory
            )

            return wsState
        }
    }
}