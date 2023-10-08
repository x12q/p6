package com.qxdzbc.p6.di

import androidx.compose.ui.geometry.Offset
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.command.CommandStack
import com.qxdzbc.p6.command.CommandStacks
import com.qxdzbc.p6.di.qualifiers.*
import com.qxdzbc.p6.ui.workbook.di.DefaultCommandStackMs
import com.qxdzbc.p6.ui.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultColRangeQualifier
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultRowRangeQualifier
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultSelectRectState
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultSelectRectStateMs
import com.qxdzbc.p6.ui.worksheet.select_rect.SelectRectState
import dagger.Module
import dagger.Provides

/**
 * Init defaults objects commonly used in multiple parts of the app.
 */
@Module
//@ContributesTo(P6AnvilScope::class)
interface CommonDefaultObjModule {

    /**
     * TODO for some reason I can't bind this using Anvil, must do a manual binding.
     */
//    @Binds
//    @DefaultSelectRectState
//    fun SelectRectState(i: SelectRectStateImp): SelectRectState

    companion object{
        @Provides
        @DefaultCommandStackMs
        fun defaultCommandStack():Ms<CommandStack>{
            return ms(CommandStacks.stdCommandStack())
        }

        @Provides
        @DefaultSelectRectStateMs
        fun SelectRectStateMs(@DefaultSelectRectState i: SelectRectState): Ms<SelectRectState> {
            return ms(i)
        }

        @Provides
        @NullInt
        fun nullInt(): Int? {
            return null
        }

        @Provides
        @FalseMs
        fun falseMs(): Ms<Boolean> {
            return ms(false)
        }

        @Provides
        @TrueMs
        fun trueMs(): Ms<Boolean> {
            return ms(true)
        }

        @Provides
        @False
        fun bFalse(): Boolean {
            return false
        }

        @Provides
        @True
        fun bTrue(): Boolean {
            return true
        }


        @Provides
        @DefaultColRangeQualifier
        fun defaultColRange(): IntRange {
            return WorksheetConstants.defaultColRange
        }

        @Provides
        @DefaultRowRangeQualifier
        fun defaultRowRange(): IntRange {
            return WorksheetConstants.defaultRowRange
        }

        @Provides
        @DefaultZeroOffset
        fun defaultZeroOffset(): Offset {
            return Offset(0F, 0F)
        }

    }
}

