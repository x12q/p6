package com.qxdzbc.p6.ui.script_editor.script_tree.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.document.script.*
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.common.view.tree_view.state.TreeNodeStateImp
import test.TestSample
import kotlin.test.*

class TreeNodeStateContainerImpTest {
    lateinit var cont: TreeNodeStateContainerImp
    val wbk1 = TestSample.wbk1

    val sKey11 = ScriptEntryKey("sKey11", wbk1)
    val sKey12 = ScriptEntryKey("sKey12", wbk1)
    val wbk2 = TestSample.wbk2
    val sKey21 = ScriptEntryKey("sKey21", wbk2)
    val sKey22 = ScriptEntryKey("sKey22", wbk2)
    val wbk3 = TestSample.wbk3
    val sKey31 = ScriptEntryKey("sKey31", wbk3)
    val sKey32 = ScriptEntryKey("sKey32", wbk3)

    lateinit var wbContainerMs: Ms<WorkbookContainer>
    lateinit var testSample: TestSample
    @BeforeTest
    fun b() {
       testSample = TestSample()
        wbContainerMs = testSample.sc.wbContMs
        var centralScriptCont by testSample.appState.centralScriptContainerMs

        centralScriptCont = centralScriptCont.addMultiScriptsForce(
            listOf("appScript1", "appScript2").map {
                ScriptEntry(
                    key = ScriptEntryKey(it, null),
                    script = it + "asdasd"
                )
            }+listOf(sKey11,sKey12).map {
                ScriptEntry(
                    key = it,
                    script = it.toString() + "asdasd"
                )
            }+listOf(sKey21,sKey22).map {
                ScriptEntry(
                    key = it,
                    script = it.toString() + "asdasd"
                )
            }+listOf(sKey31,sKey32).map {
                ScriptEntry(
                    key = it,
                    script = it.toString() + "asdasd"
                )
            }
        )

        cont = TreeNodeStateContainerImp(
            centralScriptContainerMs = testSample.appState.centralScriptContainerMs,
            checkWbExist = {
                wbContainerMs.value.hasWb(it)
            },
            appNodeStateMs = ms(TreeNodeStateImp(isExpandable = true)),
            wbNodeStateMsMap = listOf(wbk1, wbk2, wbk3)
                .associateWith { ms(TreeNodeStateImp(isExpandable = true)) },
            scriptNodeStateMsMap = emptyMap()
        )
    }

    @Test
    fun getScriptNodeState(){
        assertNotNull(cont.getNodeStateMs(sKey11))
//        assertNotNull(cont.getWbNodeStateMs(wbk2))
    }

    @Test
    fun removeScriptState() {
        assertNotNull(cont.getNodeStateMs(sKey21))
        assertNotNull(cont.getNodeStateMs(sKey22))
        val oldState = cont.getNodeStateMs(sKey21)
        val c2 = cont.removeScriptState(sKey21)
        assertNotEquals(oldState,c2.getNodeStateMs(sKey21))
        val oldStates =listOf(
            cont.getNodeStateMs(sKey21),
            cont.getNodeStateMs(sKey22)
        )
        val c3 = cont.removeScriptState(wbk2)
        assertNotEquals(oldStates[0],c3.getNodeStateMs(sKey22))
        assertNotEquals(oldStates[1],c3.getNodeStateMs(sKey21))
    }

    @Test
    fun replaceWorkbookKey() {
        val oldWbk = wbk2
        val newWbk = WorkbookKey("newWbkey")
        assertNull(cont.getWbNodeStateMs(newWbk))

        val state = cont.getWbNodeStateMs(oldWbk)
        val oldSKey21 = cont.getNodeStateMs(sKey21)
        val oldSKey22 = cont.getNodeStateMs(sKey22)
        val oldz=cont.scriptNodeStateMsMap.values.toList()

        wbContainerMs.value=wbContainerMs.value.replaceKey(oldWbk,newWbk)
        testSample.sc.wbStateCont = testSample.sc.wbStateCont.replaceKey(oldWbk,newWbk)
        val c2 = cont.replaceWorkbookKey(oldWbk, newWbk)


        assertNotNull(c2.getWbNodeStateMs(newWbk))
        assertEquals(state, c2.getWbNodeStateMs(newWbk))
        assertNull(c2.getNodeStateMs(sKey21))
        assertNull(c2.getNodeStateMs(sKey22))
        assertEquals(oldSKey21, c2.getNodeStateMs(sKey21.setWbKey(newWbk)))
        assertEquals(oldSKey22, c2.getNodeStateMs(sKey22.setWbKey(newWbk)))
        assertEquals(oldz, c2.scriptNodeStateMsMap.values.toList())
    }

}
