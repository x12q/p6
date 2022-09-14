//package com.qxdzbc.p6.ui.script_editor
//
//import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerImp
//import com.qxdzbc.p6.app.oddity.OddityContainerImp
//import com.qxdzbc.common.error.ErrorReport
//import com.qxdzbc.p6.message.api.message.protocol.data_interface_definition.IOPub
//import com.qxdzbc.p6.message.api.message.protocol.data_interface_definition.Shell
//import com.qxdzbc.p6.message.api.message.sender.exception.SenderErrors
//import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
//import com.qxdzbc.common.compose.Ms
//import com.qxdzbc.common.compose.StateUtils.ms
//import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainerImp2
//import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainerImp3
//import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
//import com.qxdzbc.p6.ui.script_editor.state.SwingCodeEditorStateImp
//import org.mockito.kotlin.mock
//import org.mockito.kotlin.times
//import org.mockito.kotlin.verify
//import test.TestSample
//import kotlin.test.BeforeTest
//import kotlin.test.Test
//import kotlin.test.assertTrue
//
//class ScriptEditorErrorRouterImpTest {
//    lateinit var router: ScriptEditorErrorRouterImp
//    lateinit var errorRouter: ErrorRouter
//    lateinit var codeEditorStateMs:Ms<CodeEditorState>
//    val oddityContainer = OddityContainerImp()
//    @BeforeTest
//    fun b(){
//        codeEditorStateMs = ms(SwingCodeEditorStateImp(
//            wbContMs = ms(WorkbookContainerImp()),
//            centralScriptContainerMs = ms(CentralScriptContainerImp3())
//        ))
//        errorRouter = mock()
//        router = ScriptEditorErrorRouterImp(
//            codeEditorStateMs = codeEditorStateMs,
//            errorRouter = errorRouter
//        )
//    }
//
//    @Test
//    fun toOutputPanel_toPanel() {
//        val expectations = listOf(
//            "trace1${System.lineSeparator()}trace2",
//            "trace3${System.lineSeparator()}trace4",
//            "a message",
//            "oooo",
//        )
//        val eList = listOf (
//            ErrorReport(
//                header = SenderErrors.CodeError.header,
//                data = SenderErrors.CodeError.Data(
//                    Shell.Execute.Reply.Content(
//                        traceback = listOf("trace1","trace2")
//                    )
//                )
//            ),
//            ErrorReport(
//                header = SenderErrors.IOPubExecuteError.header,
//                data = SenderErrors.IOPubExecuteError.Data(
//                    IOPub.ExecuteError.Content(
//                        traceback = listOf("trace3","trace4")
//                    )
//                )
//            ),
//            ErrorReport(
//                header = SenderErrors.CodeError.header,
//                data = "a message"
//            ),
//            ErrorReport(
//                header = SenderErrors.IOPubExecuteError.header,
//                data = object : Any() {
//                    override fun toString(): String {
//                        return "oooo"
//                    }
//                }
//            )
//        )
//
//        for ((i,e) in eList.withIndex()){
//            router.toOutputPanel(e)
//            assertTrue { expectations[i] in codeEditorStateMs.value.outputList }
//        }
//    }
//
//
//    @Test
//    fun toCodeEditorWindow() {
//        assertTrue { codeEditorStateMs.value.oddityContainer.isEmpty() }
//        router.toCodeEditorWindow(
//            TestSample.sampleErrorReport
//        )
//        assertTrue { codeEditorStateMs.value.oddityContainer.isNotEmpty() }
//        assertTrue { codeEditorStateMs.value.oddityContainer.containErrorReport(TestSample.sampleErrorReport) }
//    }
//
//    @Test
//    fun toApp() {
//        router.toApp(
//            TestSample.sampleErrorReport
//        )
//        verify (errorRouter, times(1)).toApp(TestSample.sampleErrorReport)
//    }
//}
