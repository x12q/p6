package  com

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.emeraldblast.p6.app.app_context.P6GlobalAccessPoint
import com.emeraldblast.p6.app.common.utils.Loggers
import com.emeraldblast.p6.di.DaggerP6Component
import com.emeraldblast.p6.di.P6Component
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookImp
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.oddity.OddityContainer
import com.emeraldblast.p6.app.oddity.OddityType
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelConfigImp
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelContext
import com.emeraldblast.p6.message.di.DaggerMessageApiComponent
import com.emeraldblast.p6.message.di.MessageApiComponent
import com.emeraldblast.p6.ui.common.R
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.MsUtils.toMs
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.common.compose.rms
import com.emeraldblast.p6.ui.common.view.MBox
import com.emeraldblast.p6.ui.common.view.dialog.error.ErrorDialogWithStackTrace
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookStateFactory.Companion.createRefresh
import com.emeraldblast.p6.ui.script_editor.ScriptEditor
import com.emeraldblast.p6.ui.theme.P6DefaultTypoGraphy
import com.emeraldblast.p6.ui.theme.P6LightColors2
import com.emeraldblast.p6.ui.window.WindowView
import com.emeraldblast.p6.ui.window.state.ActiveWorkbookPointerImp
import com.emeraldblast.p6.ui.window.state.WindowState
import com.github.michaelbull.result.*
import kotlinx.coroutines.*
import java.nio.file.Paths
import kotlin.collections.fold

//
fun main() {
    runBlocking {
        val cs = this
        var outKernelContext: KernelContext? = null
        var p6Comp2: P6Component? = null
        application {
            val appScope = this
            var starting by rms(true)
            // x: initialize the app
            LaunchedEffect(Unit) {
                val kernelCoroutineScope: CoroutineScope = cs
                val msgApiComponent: MessageApiComponent = DaggerMessageApiComponent.builder()
                    .kernelCoroutineScope(kernelCoroutineScope)
                    .networkServiceCoroutineDispatcher(Dispatchers.Default)
                    .serviceLogger(Loggers.serviceLogger)
                    .msgApiCommonLogger(Loggers.msgApiCommonLogger)
                    .apply {
                        val defaultKernelConfigRs = KernelConfigImp.fromFile(Paths.get(R.defaultPythonConfigFile))
                        if (defaultKernelConfigRs is Ok) {
                            this.kernelConfig(defaultKernelConfigRs.value)
                        }
                    }
                    .build()

                val p6Comp: P6Component = DaggerP6Component.builder()
                    .username("user_name")
                    .messageApiComponent(msgApiComponent)
                    .applicationCoroutineScope(kernelCoroutineScope)
                    .applicationScope(appScope)
                    .build()

                val appStateMs = p6Comp.appStateMs()
                var appState by appStateMs
                appState = run {
                    val wb1: Workbook = WorkbookImp(
                        keyMs = WorkbookKey("Book1", null).toMs(),
                    ).let {
                        listOf("Sheet1", "Sheet2").fold(it) { acc, name ->
                            acc.createNewWs(name) as WorkbookImp
                        }
                    }
                    val wb2 = WorkbookImp(
                        keyMs = WorkbookKey("Book2", null).toMs(),
                    ).let {
                        listOf("Sheet1", "Sheet2").fold(it) { acc, name ->
                            acc.createNewWs(name) as WorkbookImp
                        }
                    }
                    val wbStateMs1: Ms<WorkbookState> = ms(
                        p6Comp.workbookStateFactory().createRefresh(
                            wbMs = ms(wb1)
                        )
                    )
                    val wbStateMs2: Ms<WorkbookState> = ms(
                        p6Comp.workbookStateFactory().createRefresh(
                            wbMs = ms(wb2)
                        )
                    )
                    appState.globalWbStateContMs.apply {
                        this.value = this.value.addWbState(wbStateMs1).addWbState(wbStateMs2)
                    }
                    val zz = listOf(
                        ms(
                            p6Comp.windowStateFactory().create(
                                wbKeySet = listOf(wbStateMs1, wbStateMs2).map { it.value.wbKey }.toSet(),
                                activeWorkbookPointerMs = ms(
                                    ActiveWorkbookPointerImp(
                                        listOf(
                                            wbStateMs1,
                                            wbStateMs2
                                        ).map { it.value.wbKey }.toSet().firstOrNull()
                                    )
                                )
                            ) as WindowState
                        )
                    ).fold(appState.stateCont)
                    { acc, e ->
                        acc.addWindowState(e)
                    }
                    appState.stateCont = zz
                    appState
                }

                p6Comp2 = p6Comp

                P6GlobalAccessPoint.setP6Component(p6Comp)

                val p6RpcServer = p6Comp.p6RpcServer()
                val kernelContext: KernelContext = p6Comp.kernelContext()
                val kernelSM = p6Comp.kernelServiceManager()
                outKernelContext = p6Comp.kernelContext()
                cs.launch(Dispatchers.Default) {
                    p6RpcServer.start()
                }
                cs.launch {
                    delay(2000)
                    println("RPC server on ${p6RpcServer.server?.port}")
                    println("RPC is running: ${!(p6RpcServer.server?.isShutdown ?: true)}")
                }


                Runtime.getRuntime().addShutdownHook(Thread {
                    // x: kill kernel context when jvm stops
                    runBlocking {
                        p6RpcServer.stop()
                        kernelContext.stopAll()
                    }
                })

                val kernelStartRs: Result<Unit, ErrorReport> = kernelContext.startAll().andThen {
                    kernelSM.startAll()
                }
                if (kernelStartRs is Err) {
                    println("Cant start kernel")
                    println(kernelStartRs)
                } else {
                    val serviceStartRS = kernelSM.startAll()
                    if (serviceStartRS is Err) {
                        println("Cant start kernel service")
                        println(serviceStartRS)
                    }
                }
                starting = false
            }
            MaterialTheme(colors = P6LightColors2, typography = P6DefaultTypoGraphy) {
                if (!starting) {
                    val p6Comp3 = p6Comp2
                    if (p6Comp3 != null) {
                        val appStateMs2 = remember { p6Comp3.appStateMs() }
                        P6GlobalAccessPoint.setAppStateMs(appStateMs2)
                        val appState = appStateMs2.value


                        for (windowStateMs in appState.windowStateMsList) {
                            val windowState = windowStateMs.value
                            val windowAction = p6Comp3.windowActionTable().getWindowAction()
                            val windowActionTable = p6Comp3.windowActionTable()
                            WindowView(
                                state = windowState,
                                windowActionTable = windowActionTable,
                                windowAction = windowAction,
                            )
                        }

                        if (appState.codeEditorIsOpen) {
                            val appAction = p6Comp3.appAction()
                            Window(onCloseRequest = {
                                appAction.closeCodeEditor()
                            }, title = "Code Editor") {
                                ScriptEditor(
                                    state = appState.codeEditorState,
                                    actionTable = p6Comp3.codeEditorActionTable()
                                )
                            }
                        }

                        var appOddityContainer: OddityContainer by appState.oddityContainerMs
                        if (appOddityContainer.isNotEmpty()) {
                            for (bugMsg in appOddityContainer.oddList) {
                                Window(
                                    onCloseRequest = { },
                                    title = "X",
                                ) {
                                    ErrorDialogWithStackTrace(
                                        oddMsg = bugMsg,
                                        onOkClick = {
                                            when (bugMsg.type) {
                                                OddityType.FATAL -> {
                                                    appOddityContainer.remove(bugMsg)
                                                    // x: Kill app when encounter fatal error
                                                    p6Comp3.appAction().exitApp()
                                                }
                                                else -> appOddityContainer = appOddityContainer.remove(bugMsg)
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    }
                } else {
                    val windowState = rememberWindowState(size = DpSize(100.dp, 100.dp))
                    Window(
                        onCloseRequest = { },
                        title = "Starting",
                        state = windowState
                    ) {
                        MBox(modifier = Modifier.background(Color.Cyan)) {
                            Text("Starting")
                        }
                    }
                }

            }
        }
        outKernelContext?.stopAll()
    }
}
