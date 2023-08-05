package test

import androidx.compose.runtime.getValue
import com.qxdzbc.common.Rs
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.p6.app.action.common_data_structure.ErrorIndicator
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.window.state.WindowState
import com.qxdzbc.p6.ColdInit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.BoolUnit.Companion.TRUE
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import test.di.DaggerTestComponent
import test.di.TestComponent


class TestSample : TestAppScope {
    val activeWindowPointer get() = comp.activeWindowPointer

    val wb1Ws1St get() = this.sc.getWbWsSt(wbKey1, wsn1)!!
    val wb2Ws1St get() = this.sc.getWbWsSt(wbKey2, wsn1)!!

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
        val sampleErrorReport = SingleErrorReport(
            header = ErrorHeader(
                errorCode = "123", errorDescription = "test sample error"
            )
        )
        val sampleErrIndicator = ErrorIndicator.error(sampleErrorReport)
        val mockTranslator = object : P6Translator<ExUnit> {
            override fun translate(formula: String): Rs<ExUnit, SingleErrorReport> {
                return TRUE.toOk()
            }
        }

        fun mockTranslatorGetter(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit> {
            return mockTranslator
        }

        fun mockTranslatorGetter2(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): P6Translator<ExUnit> {
            return mockTranslator
        }

        fun mockTranslatorGetter3(wbwsSt: WbWsSt): P6Translator<ExUnit> {
            return mockTranslator
        }
    }

    val window1Id: String get() = sc.windowStateMsList[0].id
    val window2Id: String get() = sc.windowStateMsList[1].id

    val kernelCoroutineScope: CoroutineScope = GlobalScope

    override val comp: TestComponent = DaggerTestComponent.builder()
        .username("user_name")
        .applicationCoroutineScope(kernelCoroutineScope)
        .applicationScope(null)
        .build()

    val errorRouter = comp.errorRouter()

    val mockTranslator = object : P6Translator<ExUnit> {
        override fun translate(formula: String): Rs<ExUnit, SingleErrorReport> {
            return TRUE.toOk()
        }
    }


    private fun makeSampleWbState1(): WorkbookState {
        return comp.workbookStateFactory().create(
            wbMs = ms(WorkbookImp(
                keyMs = wbKey1Ms,
            ).apply {
                addMultiSheetOrOverwrite(
                    listOf(
                        WorksheetImp(wsn1.toMs(), wbKeySt = wbKey1Ms),
                        WorksheetImp(wsn2.toMs(), wbKey1Ms)
                    )
                )
            }),
            null
        )
    }

    private fun makeSampleWBState(wbKeyMs: Ms<WorkbookKey>): WorkbookState {
        return comp.workbookStateFactory().create(
            wbMs =
            ms(WorkbookImp(
                keyMs = wbKeyMs,
            ).apply {
                addMultiSheetOrOverwrite(
                    listOf<Worksheet>(
                        WorksheetImp(wsn1.toMs(), wbKey2Ms),
                        WorksheetImp(wsn2.toMs(), wbKey2Ms)
                    )
                )
            }),
            null
        )
    }

    private fun setupSampleWbStateContMs(): WorkbookStateContainer {
        return sc.wbStateCont.apply {
            removeAll()
            addOrOverwriteWbState(makeSampleWbState1())
            addOrOverwriteWbState(makeSampleWBState(wbKey2Ms))
            addOrOverwriteWbState(makeSampleWBState(wbKey3Ms))
            addOrOverwriteWbState(makeSampleWBState(wbKey4Ms))
        }
    }

    private fun makeSampleWindowStateMs1(): Ms<OuterWindowState> {
        val inner: WindowState = comp.windowStateFactory().createDefault().apply {
            addWbKey(wbKey1Ms)
            addWbKey(wbKey2Ms)
            activeWbPointer = activeWbPointer.pointTo(wbKey1Ms)
        }

        return ms(comp.outerWindowStateFactory().create(inner))
    }

    private fun makeSampleWindowStateMs2(): Ms<OuterWindowState> {

        val inner: WindowState = comp.windowStateFactory().createDefault()
            .apply {
                addWbKey(wbKey3Ms)
                addWbKey(wbKey4Ms)
                activeWbPointer = activeWbPointer.pointTo(wbKey3Ms)
            }

        return ms(comp.outerWindowStateFactory().create(inner))
    }

    override val ts: TestSample = this
    override var appState = comp.appState()
    override val sc: StateContainer
        get() = comp.stateContainer

    val sampleWindowStateMs get() = sc.windowStateMsList.get(0)

    val wbCont = sc.wbCont

    val wb1Ms get() = this.wbCont.getWbMs(wbKey1Ms)!!
    val wb1 get() = wb1Ms.value
    val wb2Ms get() = this.wbCont.getWbMs(wbKey2Ms)!!
    val wb2 get() = wb2Ms.value

    init {
        setupSampleWbStateContMs()
        val windowState1 = makeSampleWindowStateMs1()
        val windowState2 = makeSampleWindowStateMs2()
        appState.stateCont.apply {
            addOuterWindowState(windowState1)
            addOuterWindowState(windowState2)
        }
        ts.activeWindowPointer.pointTo(
            windowState1.value.windowId
        )

        val ci = ColdInit()
    }

    fun sampleAppState() = appState
}

