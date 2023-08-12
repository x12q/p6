package com.qxdzbc.p6.di

import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.file_util.FileUtil
import com.qxdzbc.common.file_util.FileUtilImp
import com.qxdzbc.p6.app_context.AppContext
import com.qxdzbc.p6.app_context.AppContextImp
import com.qxdzbc.p6.common.utils.Utils
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.di.qualifiers.*
import com.qxdzbc.p6.ui.app.di.AppStateModule
import com.qxdzbc.p6.ui.window.status_bar.di.StatusBarModule
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouterImp
import com.qxdzbc.p6.ui.app.action.AppAction
import com.qxdzbc.p6.ui.app.action.AppActionImp
import com.qxdzbc.p6.ui.common.color_generator.*
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        UtilModule::class,
        TranslatorModule::class,
        AppStateModule::class,
        StatusBarModule::class,
        CoroutineModule::class,
        CommonDefaultObjModule::class,
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
        fun fq(): FocusRequester {
            return FocusRequester()
        }

        @Provides
        @Singleton
        @EventServerPort
        fun eventServerPort(): Int {
            val eventServerPort = Utils.findSocketPort()
            return eventServerPort
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
