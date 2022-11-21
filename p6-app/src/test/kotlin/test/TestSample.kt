package test

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rs
import com.qxdzbc.p6.app.common.utils.Loggers
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.p6.app.action.common_data_structure.ErrorIndicator
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.script.ScriptEntry
import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelConfigImp
import com.qxdzbc.p6.message.di.DaggerMessageApiComponent
import com.qxdzbc.p6.message.di.MessageApiComponent
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory.Companion.createRefresh
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.window.state.WindowState
import com.qxdzbc.p6.ui.window.state.WindowStateFactory.Companion.createDefault
import com.github.michaelbull.result.unwrap
import com.qxdzbc.p6.ColdInit
import com.qxdzbc.p6.translator.formula.execution_unit.BoolUnit.Companion.TRUE
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import test.di.DaggerTestComponent
import test.di.WindowStateModuleForTest
import java.nio.file.Paths


class TestSample: TestAppScope {
    val wsn1 = TestSample.wsn1
    val wsn2 = TestSample.wsn2

    val wbKey1Ms = wbk1.toMs()
    val wbKey2Ms = wbk2.toMs()
    val wbKey3Ms = wbk3.toMs()
    val wbKey4Ms = wbk4.toMs()

    val wbKey1 by wbKey1Ms
    val wbKey2 by wbKey2Ms
    val wbKey3 by wbKey3Ms
    val wbKey4 by wbKey4Ms

    companion object {
        val wsn1 = "Sheet1"
        val wsn2 = "Sheet2"
        val wbk2: WorkbookKey = WorkbookKey("Book2", null)
        val wbk1: WorkbookKey = WorkbookKey("Book1", null)
        val wbk3 = WorkbookKey("Book3", null)
        val wbk4 = WorkbookKey("Book4", null)
        val sampleErrorReport = ErrorReport(
            header = ErrorHeader(
                errorCode = "123", errorDescription = "test sample error"
            )
        )
        val sampleErrIndicator = ErrorIndicator.error(sampleErrorReport)
        val mockTranslator = object : P6Translator<ExUnit> {
            override fun translate(formula: String): Rs<ExUnit, ErrorReport> {
                return TRUE.toOk()
            }
        }

        fun mockTranslatorGetter(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit> {
            return mockTranslator
        }
        fun mockTranslatorGetter2(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit> {
            return mockTranslator
        }
        fun mockTranslatorGetter3(wbwsSt:WbWsSt): P6Translator<ExUnit> {
            return mockTranslator
        }
    }

    val window1Id:String get() = sc.windowStateMsList[0].value.id
    val window2Id:String get() = sc.windowStateMsList[1].value.id

    val kernelCoroutineScope: CoroutineScope = GlobalScope
    val msgApiComponent: MessageApiComponent = DaggerMessageApiComponent.builder()
        .kernelConfig(KernelConfigImp.fromFile(Paths.get("jupyterConfig.json")).unwrap())
        .kernelCoroutineScope(kernelCoroutineScope)
        .networkServiceCoroutineDispatcher(Dispatchers.Default)
        .serviceLogger(Loggers.serviceLogger)
        .msgApiCommonLogger(Loggers.msgApiCommonLogger)
        .build()

    override val comp = DaggerTestComponent.builder()
        .username("user_name")
        .messageApiComponent(msgApiComponent)
        .applicationCoroutineScope(kernelCoroutineScope)
        .applicationScope(null)
        .build()

    val errorRouter = comp.errorRouter()

    val mockTranslator = object : P6Translator<ExUnit> {
        override fun translate(formula: String): Rs<ExUnit, ErrorReport> {
            return TRUE.toOk()
        }
    }

    val eb11 = ScriptEntry(
        key = ScriptEntryKey(
            wbKey = wbk1,
            name = "1",
        ), script = "script_b11"
    )
    val eb12 = ScriptEntry(
        key = ScriptEntryKey(
            wbKey = wbk1,
            name = "2",
        ), script = "script_b12"
    )
    val eb21 = ScriptEntry(
        key = ScriptEntryKey(
            wbKey = wbk2,
            name = "1",
        ), script = "script_b21"
    )
    val eb22 = ScriptEntry(
        key = ScriptEntryKey(
            wbKey = wbk2,
            name = "2",
        ), script = "script_b22"
    )
    val appS1 = ScriptEntry(
        key = ScriptEntryKey(
            wbKey = null, name = "1"
        ), script = "appS1"
    )
    val appS2 = ScriptEntry(
        key = ScriptEntryKey(
            wbKey = null, name = "2"
        ), script = "appS2"
    )

    private fun makeSampleWbState1(): Ms<WorkbookState> {
        return ms(
            comp.workbookStateFactory().createRefresh(
                wbMs = ms(
                    WorkbookImp(
                        keyMs = wbKey1Ms,
                    ).addMultiSheetOrOverwrite(
                        listOf(
                            WorksheetImp(wsn1.toMs(), wbKeySt = wbKey1Ms)
//                                .addOrOverwrite(
//                                    IndCellImp(
//                                        address = CellAddress("A1"),
//                                        content = CellContentImp(cellValueMs = 1.0.toCellValue().toMs())
//                                    )
//                                )
//                                .addOrOverwrite(
//                                    IndCellImp(
//                                        address = CellAddress("B2"),
//                                        content = CellContentImp(cellValueMs = 2.0.toCellValue().toMs())
//                                    )
//                                )
//                                .addOrOverwrite(
//                                    IndCellImp(
//                                        address = CellAddress("C3"),
//                                        content = CellContentImp(cellValueMs = 3.0.toCellValue().toMs())
//                                    )
//                                ).addOrOverwrite(
//                                    IndCellImp(
//                                        address = CellAddress("D4"),
//                                        content = CellContentImp(cellValueMs = "abc".toCellValue().toMs())
//                                    )
//                                ),
                            ,
                            WorksheetImp(wsn2.toMs(), wbKey1Ms)
                        )
                    )
                )
            )
        )
    }

    private fun makeSampleWBState(wbKeyMs: Ms<WorkbookKey>): Ms<WorkbookState> {
        return ms(
            comp.workbookStateFactory().createRefresh(
                wbMs = ms(
                    WorkbookImp(
                        keyMs = wbKeyMs,
                    ).addMultiSheetOrOverwrite(
                        listOf<Worksheet>(
                            WorksheetImp(wsn1.toMs(), wbKey2Ms),
                            WorksheetImp(wsn2.toMs(), wbKey2Ms)
                        )
                    )
                )
            )
        )
    }

    private fun makeSampleWbStateContMs(): WorkbookStateContainer {
        return sc.wbStateCont.removeAll()
            .addOrOverwriteWbState(makeSampleWbState1())
            .addOrOverwriteWbState(makeSampleWBState(wbKey2Ms))
            .addOrOverwriteWbState(makeSampleWBState(wbKey3Ms))
            .addOrOverwriteWbState(makeSampleWBState(wbKey4Ms))
    }

    private fun makeSampleWindowStateMs1(): Ms<OuterWindowState> {
        val inner:Ms<WindowState> = ms(
            comp.windowStateFactory().createDefault(
                listOf(wbKey1Ms, wbKey2Ms).toSet()
            ),
        )
        return ms(comp.outerWindowStateFactory().create(inner))
    }

    private fun makeSampleWindowStateMs2(): Ms<OuterWindowState> {

        val inner :Ms<WindowState> = ms(
            comp.windowStateFactory().createDefault(
                listOf(wbKey3Ms, wbKey4Ms).toSet()
            ),
        )
        return ms(comp.outerWindowStateFactory().create(inner))
    }

    val appStateMs = comp.appStateMs()
    override val ts: TestSample=this
    override var appState by appStateMs
    override val sc: StateContainer
        get() = this.stateCont
    override val scMs: Ms<StateContainer>
        get() = this.stateContMs


    val sampleCodeContainer by appState.centralScriptContainerMs
    val sampleCodeContainerMs get() = appState.centralScriptContainerMs
    val sampleWindowStateMs get() = sc.windowStateMsList.get(0)

    val wbContMs = sc.wbContMs

    val wb1Ms get()= this.wbContMs.value.getWbMs(wbKey1Ms)!!
    val wb1 get() =wb1Ms.value
    val wb2Ms get()= this.wbContMs.value.getWbMs(wbKey2Ms)!!
    val wb2 get()= wb2Ms.value

    init {
        sc.wbStateContMs.value = makeSampleWbStateContMs()
        val windowState1 = makeSampleWindowStateMs1()
        val windowState2= makeSampleWindowStateMs2()
        appState.subAppStateContMs.value = appState.subAppStateContMs.value
            .addOuterWindowState(windowState1)
            .addOuterWindowState(windowState2)
        appState.activeWindowPointer = appState.activeWindowPointer.pointTo(
            windowState1.value.windowId
        )

        appState.centralScriptContainer = appState.centralScriptContainer.addMultiScriptsForce(
            listOf(appS1, appS2, eb21, eb22, eb11, eb12)
        )
        val q = ColdInit()
    }

    fun sampleAppStateMs() = appStateMs
    fun sampleAppStateMs(wbCont: WorkbookContainer): Ms<AppState> {
        sc.wbCont = wbCont
        return appStateMs
    }

    fun stateContMs(): MutableState<StateContainer> {
        return comp.stateContMs()
    }
    val stateContMs get() = stateContMs()
    val stateCont get()=stateContMs().value
}

