package com.qxdzbc.p6.ui.document.worksheet.cursor.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorIdImp
import com.qxdzbc.p6.ui.document.worksheet.di.comp.WsScope
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface CursorModule {

    @Binds
    @WsScope
    fun cursorIdSt(i:Ms<CursorId>): St<@JvmSuppressWildcards CursorId>

    @Binds
    @WsScope
    @MainCellMs
    fun mainCellSt(@MainCellMs i:Ms<CellAddress>): St<@JvmSuppressWildcards CellAddress>

    companion object{
        @Provides
        @WsScope
        fun cursorIdMs(wsMs:Ms<Worksheet>): Ms<CursorId> {
            val worksheet = wsMs.value
            val wsIdMs: St<WorksheetId> = worksheet.idMs
            val cursorIdMs: Ms<CursorId> = StateUtils.ms(
                CursorIdImp(wsIdMs)
            )
            return cursorIdMs
        }

        @Provides
        @WsScope
        @MainCellMs
        fun mainCellMs(): Ms<CellAddress> = StateUtils.ms(CellAddresses.A1)
    }

}