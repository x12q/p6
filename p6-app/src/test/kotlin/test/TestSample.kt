package test

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rs
import com.qxdzbc.p6.app.common.utils.Loggers
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.p6.app.action.common_data_structure.ErrorIndicator
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.di.DaggerP6Component
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.IndCellImp
import com.qxdzbc.p6.app.document.cell.d.CellValue.Companion.toCellValue
import com.qxdzbc.p6.app.document.cell.d.CellContentImp
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import java.nio.file.Paths


class TestSample {
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
                return ExUnit.TRUE.toOk()
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

    val window1Id:String get() = appState.windowStateMsList[0].value.id
    val window2Id:String get() = appState.windowStateMsList[1].value.id

    val kernelCoroutineScope: CoroutineScope = GlobalScope
    val msgApiComponent: MessageApiComponent = DaggerMessageApiComponent.builder()
        .kernelConfig(KernelConfigImp.fromFile(Paths.get("jupyterConfig.json")).unwrap())
        .kernelCoroutineScope(kernelCoroutineScope)
        .networkServiceCoroutineDispatcher(Dispatchers.Default)
        .serviceLogger(Loggers.serviceLogger)
        .msgApiCommonLogger(Loggers.msgApiCommonLogger)
        .build()

    val p6Comp = DaggerP6Component.builder()
        .username("user_name")
        .messageApiComponent(msgApiComponent)
        .applicationCoroutineScope(kernelCoroutineScope)
        .applicationScope(null)
        .build()

    val errorRouter = p6Comp.errorRouter()

    val mockTranslator = object : P6Translator<ExUnit> {
        override fun translate(formula: String): Rs<ExUnit, ErrorReport> {
            return ExUnit.TRUE.toOk()
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
            p6Comp.workbookStateFactory().createRefresh(
                wbMs = ms(
                    WorkbookImp(
                        keyMs = wbKey1Ms,
                    ).addMultiSheetOrOverwrite(
                        listOf(
                            WorksheetImp(wsn1.toMs(), wbKeySt = wbKey1Ms)
                                .addOrOverwrite(
                                    IndCellImp(
                                        address = CellAddress("A1"),
                                        content = CellContentImp(cellValueMs = 1.0.toCellValue().toMs())
                                    )
                                )
                                .addOrOverwrite(
                                    IndCellImp(
                                        address = CellAddress("B2"),
                                        content = CellContentImp(cellValueMs = 2.0.toCellValue().toMs())
                                    )
                                )
                                .addOrOverwrite(
                                    IndCellImp(
                                        address = CellAddress("C3"),
                                        content = CellContentImp(cellValueMs = 3.0.toCellValue().toMs())
                                    )
                                ).addOrOverwrite(
                                    IndCellImp(
                                        address = CellAddress("D4"),
                                        content = CellContentImp(cellValueMs = "abc".toCellValue().toMs())
                                    )
                                ),
                            WorksheetImp(wsn2.toMs(), wbKey1Ms)
                        )
                    )
                )
            )
        )
    }

    private fun makeSampleWBState(wbKeyMs: Ms<WorkbookKey>): Ms<WorkbookState> {
        return ms(
            p6Comp.workbookStateFactory().createRefresh(
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
        return appState.wbStateCont.removeAll()
            .addOrOverwriteWbState(makeSampleWbState1())
            .addOrOverwriteWbState(makeSampleWBState(wbKey2Ms))
            .addOrOverwriteWbState(makeSampleWBState(wbKey3Ms))
            .addOrOverwriteWbState(makeSampleWBState(wbKey4Ms))
    }

    private fun makeSampleWindowStateMs1(): Ms<WindowState> {
        return ms(
            p6Comp.windowStateFactory().createDefault(
                listOf(wbKey1Ms, wbKey2Ms).toSet()
            ),
        )
    }

    private fun makeSampleWindowStateMs2(): Ms<WindowState> {
        return ms(
            p6Comp.windowStateFactory().createDefault(
                listOf(wbKey3Ms, wbKey4Ms).toSet()
            ),
        )
    }

    val appStateMs = p6Comp.appStateMs()
    var appState by appStateMs

    val sampleCodeContainer by appState.centralScriptContainerMs
    val sampleCodeContainerMs get() = appState.centralScriptContainerMs
    val sampleWindowStateMs get() = appState.windowStateMsList.get(0)

    val wbContMs = appState.wbContMs

    val wb1Ms get()= this.wbContMs.value.getWbMs(wbKey1Ms)!!
    val wb1 get() =wb1Ms.value
    val wb2Ms get()= this.wbContMs.value.getWbMs(wbKey2Ms)!!
    val wb2 get()= wb2Ms.value

    init {
        appState.wbStateContMs.value = makeSampleWbStateContMs()
        val windowState1 = makeSampleWindowStateMs1()
        val windowState2= makeSampleWindowStateMs2()
        appState.windowStateMsList = appState.windowStateMsList + windowState1 + windowState2
        appState.activeWindowPointer = appState.activeWindowPointer.pointTo(
            windowState1.value.id
        )

        appState.centralScriptContainer = appState.centralScriptContainer.addMultiScriptsForce(
            listOf(appS1, appS2, eb21, eb22, eb11, eb12)
        )
    }

    fun sampleAppStateMs() = appStateMs
    fun sampleAppStateMs(wbCont: WorkbookContainer): Ms<AppState> {
        appState.wbCont = wbCont
        return appStateMs
    }

    fun stateContMs(): MutableState<StateContainer> {
        return p6Comp.stateContMs()
    }
}

