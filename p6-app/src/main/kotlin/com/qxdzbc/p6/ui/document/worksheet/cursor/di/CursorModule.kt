package com.qxdzbc.p6.ui.document.worksheet.cursor.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddressUtils
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.document.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.document.worksheet.cursor.di.qualifiers.MainCellState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorIdImp
import com.qxdzbc.p6.ui.document.worksheet.di.comp.WsScope
import com.qxdzbc.p6.ui.document.worksheet.di.qualifiers.*
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraint
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId
import dagger.Binds
import dagger.Module
import dagger.Provides
import org.antlr.v4.runtime.tree.ParseTree

@Module
interface CursorModule {

    @Binds
    @WsScope
    fun cursorIdSt(i:Ms<CursorId>): St<@JvmSuppressWildcards CursorId>

    @Binds
    @WsScope
    @MainCellState
    fun mainCellSt(@MainCellState i:Ms<CellAddress>): St<@JvmSuppressWildcards CellAddress>

    companion object{

        @Provides
        @WsScope
        @DefaultCursorParseTree
        fun defaultCursorParseTree():Ms<ParseTree?>{
            return ms(null)
        }

        @Provides
        @WsScope
        fun cursorIdMs(wsMs:Ms<Worksheet>): Ms<CursorId> {
            val worksheet = wsMs.value
            val wsIdMs: St<WorksheetId> = worksheet.idMs
            val cursorIdMs: Ms<CursorId> = ms(
                CursorIdImp(wsIdMs)
            )
            return cursorIdMs
        }

        @Provides
        @WsScope
        @MainCellState
        fun mainCellMs(): Ms<CellAddress> = ms(CellAddresses.A1)

        @Provides
        @Init_RangeConstraintInCursor
        fun DefaultRangeConstraint(): RangeConstraint {
            return WorksheetConstants.defaultRangeConstraint
        }

        @Provides
        @Init_ClipBoardRangeInCursor
        fun clipboardRange(): RangeAddress {
            return RangeAddressUtils.InvalidRange
        }


        @Provides
        @Init_FragmentedCellSetInCursor
        fun ecSet(): Set<CellAddress> {
            return emptySet()
        }

        @Provides
        @Init_RangeAddressSetInCursor
        fun erSet(): Set<RangeAddress> {
            return emptySet()
        }

        @Provides
        @Init_MainRangeInCursor
        fun nullRangeAddress(): RangeAddress?{
            return null
        }
    }

}