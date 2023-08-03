//package  com.qxdzbc.p6
//
//import androidx.compose.foundation.background
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Text
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.DpSize
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Window
//import androidx.compose.ui.window.application
//import androidx.compose.ui.window.rememberWindowState
//import com.qxdzbc.common.compose.StateUtils.ms
//import com.qxdzbc.common.compose.StateUtils.rms
//import com.qxdzbc.common.compose.StateUtils.toMs
//import com.qxdzbc.common.compose.view.MBox
//import com.qxdzbc.p6.app.app_context.P6GlobalAccessPoint
//import com.qxdzbc.p6.app.document.workbook.Workbook
//import com.qxdzbc.p6.app.document.workbook.WorkbookImp
//import com.qxdzbc.p6.app.document.workbook.WorkbookKey
//import com.qxdzbc.p6.app.err.ErrorContainer
//import com.qxdzbc.p6.app.err.ErrorType
//import com.qxdzbc.p6.di.DaggerP6Component
//import com.qxdzbc.p6.di.P6Component
//import com.qxdzbc.p6.ui.common.view.dialog.error.ErrorDialogWithStackTrace
//import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
//import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory.Companion.createAndRefresh
//import com.qxdzbc.p6.ui.theme.P6Theme
//import com.qxdzbc.p6.ui.window.WindowView
//import com.qxdzbc.p6.ui.window.state.ActiveWorkbookPointerImp
//import com.qxdzbc.p6.ui.window.state.OuterWindowState
//import com.qxdzbc.p6.ui.window.state.WindowState
//import kotlinx.coroutines.*
//
//fun main() {
//    runBlocking {
//        val cs = this
//        var p6Comp2: P6Component? = null
//        ColdInit()
//        application {
//            val appScope = this
//            var starting by rms(true)
//            // x: initialize the app
//            LaunchedEffect(Unit) {
//                val kernelCoroutineScope: CoroutineScope = cs
//
//                val p6Comp: P6Component = DaggerP6Component.builder()
//                    .username("user_name")
//                    .applicationCoroutineScope(kernelCoroutineScope)
//                    .applicationScope(appScope)
//                    .build()
//
//                P6GlobalAccessPoint.setP6Component(p6Comp)
//
//                val appState = p6Comp.appState()
//
//                val wb1: Workbook = WorkbookImp(
//                    keyMs = WorkbookKey("Book1", null).toMs(),
//                ).let { wb ->
//                    listOf("Sheet1", "Sheet2").forEach { name ->
//                        wb.createNewWs(name)
//                    }
//                    wb
//                }
//
//                val wb2 = WorkbookImp(
//                    keyMs = WorkbookKey("Book2", null).toMs(),
//                ).let { wb ->
//                    listOf("Sheet1", "Sheet2").forEach { name ->
//                        wb.createNewWs(name)
//                    }
//                    wb
//                }
//
//                val wbStateMs1: WorkbookState = p6Comp.workbookStateFactory().createAndRefresh(
//                    wbMs = ms(wb1)
//                )
//
//                val wbStateMs2: WorkbookState = p6Comp.workbookStateFactory().createAndRefresh(
//                    wbMs = ms(wb2)
//                )
//
//                appState.stateCont.wbStateCont.apply {
//                    addOrOverwriteWbState(wbStateMs1)
//                    addOrOverwriteWbState(wbStateMs2)
//                }
//
//                listOf(
//                    ms(
//                        p6Comp.outerWindowStateFactory().create(
//                            p6Comp.windowStateFactory().create(
//                                wbKeyMsSetMs = ms(listOf(wbStateMs1, wbStateMs2).map { it.wbKeyMs }.toSet()),
//                                activeWorkbookPointerMs = ms(
//                                    ActiveWorkbookPointerImp(
//                                        listOf(
//                                            wbStateMs1,
//                                            wbStateMs2
//                                        ).map { it.wb.keyMs }.toSet().firstOrNull()
//                                    )
//                                )
//                            ) as WindowState
//                        ) as OuterWindowState
//                    )
//                ).forEach {
//                    appState.stateCont.addOuterWindowState(it)
//                }
//
//
//                p6Comp2 = p6Comp
//
//
//                val p6RpcServer = p6Comp.p6RpcServer()
//
//                cs.launch(Dispatchers.Default) {
//                    p6RpcServer.start()
//                }
//                cs.launch {
//                    delay(2000)
//                    println("RPC server on ${p6RpcServer.server?.port}")
//                    println("RPC is running: ${!(p6RpcServer.server?.isShutdown ?: true)}")
//                }
//
//
//                Runtime.getRuntime().addShutdownHook(Thread {
//                    // x: kill kernel context when jvm stops
//                    runBlocking {
//                        p6RpcServer.stop()
//                    }
//                })
//
//                starting = false
//            }
//            P6Theme {
//                if (!starting) {
//                    val p6Comp3 = p6Comp2
//                    if (p6Comp3 != null) {
//                        val appState = p6Comp3.appState()
//
//                        for (windowOuterStateMs in appState.stateCont.outerWindowStateMsList) {
//                            val windowState = windowOuterStateMs.value
//                            val windowAction = p6Comp3.windowActionTable().windowAction
//                            val windowActionTable = p6Comp3.windowActionTable()
//                            WindowView(
//                                state = windowState,
//                                windowActionTable = windowActionTable,
//                                windowAction = windowAction,
//                            )
//                        }
//
//                        var appErrorContainer: ErrorContainer by appState.appErrorContainerMs
//                        if (appErrorContainer.isNotEmpty()) {
//                            for (bugMsg in appErrorContainer.errList) {
//                                Window(
//                                    onCloseRequest = { },
//                                    title = "X",
//                                ) {
//                                    ErrorDialogWithStackTrace(
//                                        errMsg = bugMsg,
//                                        onOkClick = {
//                                            when (bugMsg.type) {
//                                                ErrorType.FATAL -> {
//                                                    appErrorContainer.remove(bugMsg)
//                                                    // x: Kill app when encounter fatal error
//                                                    p6Comp3.appAction().exitApp()
//                                                }
//
//                                                else -> appErrorContainer = appErrorContainer.remove(bugMsg)
//                                            }
//                                        },
//                                    )
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    val windowState = rememberWindowState(size = DpSize(100.dp, 100.dp))
//                    Window(
//                        onCloseRequest = { },
//                        title = "Starting",
//                        state = windowState
//                    ) {
//                        MBox(modifier = Modifier.background(Color.Cyan)) {
//                            Text("Starting")
//                        }
//                    }
//                }
//
//            }
//        }
//    }
//}
