package com.qxdzbc.p6.app.action.worksheet.release_focus

import org.mockito.kotlin.mock
import test.TestSample
import kotlin.test.*

class RestoreWindowFocusStateImpTest {

    lateinit var testSample: TestSample
    lateinit var action: RestoreWindowFocusStateImp
    val appState get()= testSample.appStateMs.value

    /**
     * Starting condition:
     * - window(0) is focusing on cell editor
     * - app is not allowing range selector
     */
    @BeforeTest
    fun b(){
        testSample = TestSample()
        action = RestoreWindowFocusStateImp(testSample.appStateMs)
        appState.windowStateMsList.withIndex().forEach { (i,wds)->
            wds.value.focusState = wds.value.focusState.focusOnEditor()
            if(i==0){
                wds.value.focusState = wds.value.focusState.focusOnEditor()
            }
        }
    }

    @Test
    fun restoreAllWsFocus() {
        action.restoreAllWindowFocusState()
        appState.windowStateMsList.forEach { wds->
            val f = wds.value.focusState
            assertTrue { f.isCursorFocused }
            assertFalse { f.isEditorFocused }
        }
    }

    @Test
    fun setFocusConsideringRangeSelector() {
        val wbk = testSample.wbKey2Ms.value
        appState.cellEditorState = appState.cellEditorState.open(mock())
        appState.cellEditorState = appState.cellEditorState.setCurrentText("=")

        action.setFocusStateConsideringRangeSelector(wbk)

        appState.windowStateMsList.withIndex().forEach {(i,wds) ->
            if(wds.value.wbKeySet.contains(wbk)){
                val f = wds.value.focusState
//                assertTrue { f.isEditorFocused }
//                assertFalse { f.isCursorFocused }
            }
        }
    }
}
