package com.qxdzbc.p6.di

import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.file_util.FileUtil
import com.qxdzbc.common.file_util.FileUtilImp
import com.qxdzbc.p6.app.app_context.AppContext
import com.qxdzbc.p6.app.app_context.AppContextImp
import com.qxdzbc.p6.app.common.utils.Utils
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.di.state.StateModule
import com.qxdzbc.p6.ui.document.worksheet.di.DefaultColRangeQualifier
import com.qxdzbc.p6.ui.document.worksheet.di.DefaultRowRangeQualifier
import com.qxdzbc.p6.di.status_bar.StatusBarModule
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouterImp
import com.qxdzbc.p6.ui.app.action.AppAction
import com.qxdzbc.p6.ui.app.action.AppActionImp
import com.qxdzbc.p6.ui.common.color_generator.*
import com.qxdzbc.p6.ui.document.worksheet.WorksheetConstants
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        UtilModule::class,
        TranslatorModule::class,
        StatusBarModule::class,
        StateModule::class,
        CoroutineModule::class,
    ]
)
@ContributesTo(P6AnvilScope::class)
interface P6Module {

    @Binds
    @Singleton
    fun FormulaColorProvider(i:FormulaColorGeneratorImp): FormulaColorGenerator

    @Binds
    @Singleton
    fun ColorGenerator(i: RandomColorGenerator): ColorGenerator

    @Binds
    @Singleton
    fun ErrorRouter(i: ErrorRouterImp): ErrorRouter

    @Binds
    @Singleton
    fun AppAction(i: AppActionImp): AppAction

    @Binds
    @Singleton
    fun AppContext(i: AppContextImp): AppContext

    companion object {
        @Provides
        @Singleton
        fun FileUtil(): FileUtil {
            return FileUtilImp()
        }

        @Provides
        @NullInt
        fun nullInt(): Int? {
            return null
        }

        @Provides
        fun fq(): FocusRequester {
            return FocusRequester()
        }

        @Provides
        @True
        fun bTrue(): Boolean {
            return true
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
        @Singleton
        @EventServerPort
        fun eventServerPort(): Int {
            val eventServerPort = Utils.findSocketPort()
            return eventServerPort
        }

        @Provides
        @Null
        fun pNull(): Any? {
            return null
        }


        @Provides
        @DefaultTextFieldValue
        fun defaultTextField(): TextFieldValue {
            return TextFieldValue(
                text = "",
                selection = TextRange(0)
            )
        }
    }
}
