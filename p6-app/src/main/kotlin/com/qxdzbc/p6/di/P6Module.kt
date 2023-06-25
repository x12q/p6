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
import com.qxdzbc.p6.di.action.ActionModule
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.di.applier.ApplierModule
import com.qxdzbc.p6.di.document.DocumentModule
import com.qxdzbc.p6.di.request_maker.RMModule
import com.qxdzbc.p6.di.rpc.RpcModule
import com.qxdzbc.p6.di.state.StateModule
import com.qxdzbc.p6.di.state.app_state.AppStateModule
import com.qxdzbc.p6.di.state.window.WindowFocusStateModule
import com.qxdzbc.p6.di.state.ws.DefaultColRangeQualifier
import com.qxdzbc.p6.di.state.ws.DefaultRowRangeQualifier
import com.qxdzbc.p6.di.status_bar.StatusBarModule
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouterImp
import com.qxdzbc.p6.ui.app.action.AppAction
import com.qxdzbc.p6.ui.app.action.AppActionImp
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.common.color_generator.*
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        UtilModule::class,
        RMModule::class,
        ApplierModule::class,
        ActionTableModule::class,
//        AppStateModule::class,
        DocumentModule::class,
        TranslatorModule::class,
        StatusBarModule::class,
        ActionModule::class,
        RpcModule::class,
        StateModule::class,
        CoroutineModule::class,
    ]
)
@ContributesTo(P6AnvilScope::class)
interface P6Module {

    @Binds
    @P6Singleton
    fun FormulaColorProvider(i:FormulaColorGeneratorImp): FormulaColorGenerator

    @Binds
    @P6Singleton
    fun ColorGenerator(i: RandomColorGenerator): ColorGenerator

    @Binds
    @P6Singleton
    fun ErrorRouter(i: ErrorRouterImp): ErrorRouter

    @Binds
    @P6Singleton
    fun AppAction(i: AppActionImp): AppAction

    @Binds
    @P6Singleton
    fun AppContext(i: AppContextImp): AppContext

    companion object {
        @Provides
        @P6Singleton
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
            return P6R.worksheetValue.defaultColRange
        }

        @Provides
        @DefaultRowRangeQualifier
        fun defaultRowRange(): IntRange {
            return P6R.worksheetValue.defaultRowRange
        }

//        @Provides
//        @P6Singleton
//        @EventServerSocket
//        fun eventServerSocket(
//            zContext: ZContext,
//            @EventServerPort eventServerPort: Int
//        ): ZMQ.Socket {
//            val eventServerSocket = zContext.createSocket(SocketType.REQ)
//            eventServerSocket.connect("tcp://localhost:${eventServerPort}")
//            return eventServerSocket
//        }

        @Provides
        @P6Singleton
        @com.qxdzbc.p6.di.EventServerPort
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
