package com.emeraldblast.p6.di

import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.emeraldblast.p6.app.app_context.AppContext
import com.emeraldblast.p6.app.app_context.AppContextImp
import com.emeraldblast.p6.app.code.PythonCommander
import com.emeraldblast.p6.app.code.PythonCommanderImp
import com.emeraldblast.p6.app.coderunner.CodeRunner
import com.emeraldblast.p6.app.coderunner.FakeCodeRunner
import com.emeraldblast.p6.app.coderunner.PythonCodeRunner
import com.emeraldblast.p6.app.common.utils.FileUtil
import com.emeraldblast.p6.app.common.utils.FileUtilImp
import com.emeraldblast.p6.app.common.utils.Utils
import com.emeraldblast.p6.app.action.request_maker.*
import com.emeraldblast.p6.app.action.request_maker.p6msg_queue_sender.P6MsgRequestQueue
import com.emeraldblast.p6.app.action.request_maker.p6msg_queue_sender.P6MsgRequestQueueImp
import com.emeraldblast.p6.app.communication.event.P6EventTable
import com.emeraldblast.p6.app.communication.event.P6EventTableImp
import com.emeraldblast.p6.app.action.P6ResponseLegalityChecker
import com.emeraldblast.p6.app.action.P6ResponseLegalityCheckerImp
import com.emeraldblast.p6.di.state.ws.DefaultColRangeQualifier
import com.emeraldblast.p6.di.state.ws.DefaultRowRangeQualifier
import com.emeraldblast.p6.ui.app.ErrorRouter
import com.emeraldblast.p6.ui.app.ErrorRouterImp
import com.emeraldblast.p6.ui.app.action.AppAction
import com.emeraldblast.p6.ui.app.action.AppActionImp
import com.emeraldblast.p6.ui.common.R
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.kernel.KernelAction
import com.emeraldblast.p6.ui.kernel.KernelActionImp
import com.emeraldblast.p6.ui.script_editor.ScriptEditorErrorRouter
import com.emeraldblast.p6.ui.script_editor.ScriptEditorErrorRouterImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import javax.inject.Qualifier
import com.emeraldblast.p6.app.action.request_maker.QueueRequestMaker as QueueRequestMaker1

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class EmptyIntList
@Module
interface P6Module {
    @Binds
    @P6Singleton
    fun KernelAction(i: KernelActionImp): KernelAction

    @Binds
    @P6Singleton
    fun FileReader(i: FileUtilImp): FileUtil

    @Binds
    @P6Singleton
    fun P6ResponseErrorHandler(i: P6ResponseErrorHandlerImp): P6ResponseErrorHandler

    @Binds
    @P6Singleton
    fun ErrorRouter(i: ErrorRouterImp): ErrorRouter

    @Binds
    @P6Singleton
    fun AppAction(i: AppActionImp): AppAction

    @Binds
    @P6Singleton
    fun P6ResponseLegalityChecker(i: P6ResponseLegalityCheckerImp): P6ResponseLegalityChecker

    @Binds
    @P6Singleton
    fun P6MessageSender(i: P6MessageSenderImp): P6MessageSender


    @Binds
    @P6Singleton
    fun AppContext(i: AppContextImp): AppContext

    @Binds
    @P6Singleton
    fun CodeRunner(i: PythonCodeRunner): CodeRunner

    @Binds
    @P6Singleton
    @Fake
    fun FakeCodeRunner(i: FakeCodeRunner): CodeRunner

    @Binds
    @P6Singleton
    fun BackEndCommander(i: PythonCommanderImp): PythonCommander

    @Binds
    @P6Singleton
    fun ScriptEditorErrorRouter(i: ScriptEditorErrorRouterImp): ScriptEditorErrorRouter

    @Binds
    @P6Singleton
    fun P6MsgRequestQueue(i: P6MsgRequestQueueImp): P6MsgRequestQueue

    @Binds
    @P6Singleton
    fun BaseRequestMaker2(i: QueueRequestMakerImp): QueueRequestMaker1

    @Binds
    @P6Singleton
    fun TemplateRM2(i: TemplateRMSuspendImp): TemplateRMSuspend
    companion object {


        @Provides
        fun fq(): FocusRequester {
            return FocusRequester()
        }

        @Provides
        @P6Singleton
        fun P6EventTable(): P6EventTable {
            return P6EventTableImp
        }

        @Provides
        @com.emeraldblast.p6.di.True
        fun bTrue(): Boolean {
            return true
        }
        @Provides
        @FalseMs
        fun falseMs():Ms<Boolean>{
            return ms(false)
        }

        @Provides
        @TrueMs
        fun trueMs():Ms<Boolean>{
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
            return R.worksheetValue.defaultColRange
        }

        @Provides
        @DefaultRowRangeQualifier
        fun defaultRowRange(): IntRange {
            return R.worksheetValue.defaultRowRange
        }

        @Provides
        @P6Singleton
        @EventServerSocket
        fun eventServerSocket(
            zContext: ZContext,
            @EventServerPort eventServerPort: Int
        ): ZMQ.Socket {
            val eventServerSocket = zContext.createSocket(SocketType.REQ)
            eventServerSocket.connect("tcp://localhost:${eventServerPort}")
            return eventServerSocket
        }

        @Provides
        @P6Singleton
        @com.emeraldblast.p6.di.EventServerPort
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
