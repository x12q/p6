package com.emeraldblast.p6.app.document.script

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.Workbooks
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookStateFactory.Companion.createRefresh
import com.emeraldblast.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.emeraldblast.p6.ui.script_editor.code_container.CentralScriptContainerImp3
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import test.TestSample
import test.TestUtils
import kotlin.test.*

class CentralScriptContainerImpTest {
    lateinit var container: CentralScriptContainerImp3
    lateinit var eb11: ScriptEntry
    lateinit var eb12: ScriptEntry
    lateinit var eb21: ScriptEntry
    lateinit var eb22: ScriptEntry
    lateinit var appS1: ScriptEntry
    lateinit var appS2: ScriptEntry
    lateinit var wbkey1: WorkbookKey
    lateinit var wbkey2: WorkbookKey

    @BeforeTest
    fun b() {
        val testSample = TestSample()
        val wbStateFactory = testSample.p6Comp.workbookStateFactory()
        wbkey1 = WorkbookKey("b1")
        wbkey2 = WorkbookKey("b2")
        eb11 = ScriptEntry(
            key = ScriptEntryKey(
                wbKey = wbkey1,
                name = "1",
            ),
            script = "script_b11"
        )
        eb12 = ScriptEntry(
            key = ScriptEntryKey(
                wbKey = wbkey1,
                name = "2",
            ),
            script = "script_b12"
        )
        eb21 = ScriptEntry(
            key = ScriptEntryKey(
                wbKey = wbkey2,
                name = "1",
            ),
            script = "script_b21"
        )
        eb22 = ScriptEntry(
            key = ScriptEntryKey(
                wbKey = wbkey2,
                name = "eb22",
            ),
            script = "script_b22"
        )
        appS1 = ScriptEntry(
            key = ScriptEntryKey(
                wbKey = null,
                name = "1"
            ),
            script = "appS1"
        )
        appS2 = ScriptEntry(
            key = ScriptEntryKey(
                wbKey = null,
                name = "appS2"
            ),
            script = "appS2"
        )

        fun makeWbState(wbkey1: WorkbookKey, scMs: ScriptContainer): WorkbookState {
            val wss = wbStateFactory.createRefresh(
                wbMs=ms(Workbooks.empty(wbkey1))
            )
            wss.scriptCont = scMs
            return wss
        }

        val wbsCont: Ms<WorkbookStateContainer> = testSample.appState.globalWbStateContMs.also {
            val wss1 = makeWbState(
                wbkey1, ScriptContainerImp(
                    mapOf(
                        eb11.name to eb11.script,
                        eb12.name to eb12.script,
                    )
                )
            )
            val wss2 = makeWbState(
                wbkey2, ScriptContainerImp(
                    mapOf(
                        eb21.name to eb21.script,
                        eb22.name to eb22.script,
                    )
                )
            )
            it.value =it.value.removeAll().addWbState(ms(wss1)).addWbState(ms(wss2))
        }
        container = CentralScriptContainerImp3(
            appScriptContainerMs = ms(
                ScriptContainerImp(
                    mapOf(
                        appS1.name to appS1.script,
                        appS2.name to appS2.script
                    )
                )
            ),
            wbStateContMs = wbsCont
        )
    }

    @Test
    fun replaceKey() {
        val t = eb22
        val newName = "newName123"
        val newKey = t.key.copy(name = newName)
        assertNull(this.container.getScript(newKey))
        val c = this.container.replaceScriptKeyRs(t.key, newKey)
        assertTrue { c is Ok }
        assertNotNull(c.component1()?.getScript(newKey))

        val c2 = this.container.replaceScriptKeyRs(eb22.key, appS1.key)
        assertTrue { c2 is Err }

        val c3 = this.container.replaceScriptKeyRs(
            oldKey = ScriptEntryKey(
                name = "Non existing key"
            ),
            newKey = newKey
        )
        assertTrue { c3 is Err }

        val newEb22Key = eb22.key.copy(
            wbKey = null
        )
        assertNull(this.container.getScript(newEb22Key))
        val c4 = this.container.replaceScriptKeyRs(
            oldKey = newKey,
            newKey = newEb22Key
        )
        assertTrue { c4 is Ok }
        assertNotNull(c4.component1()!!.getScript(newEb22Key))

    }

    @Test
    fun removeScript() {
        assertTrue { this.container.contains(eb11.key) }
        val oldScriptList = this.container.getScripts(eb11.key.wbKey)
        val c1 = this.container.removeScript(eb11.key)
        assertFalse { c1.contains(eb11.key) }
        val scriptList1 = c1.getScripts(eb11.key.wbKey)
        assertTrue { TestUtils.compare2ListIgnoreOrder(oldScriptList - eb11, scriptList1) }
        assertEquals(this.container.allAppScripts, c1.allAppScripts)

        val oldAppScripts = this.container.allAppScripts
        assertTrue { this.container.contains(appS1.key) }
        val c2 = this.container.removeScript(appS1.key)
        val appScripts2 = c2.allAppScripts
        assertTrue { TestUtils.compare2ListIgnoreOrder(oldAppScripts - appS1, appScripts2) }
        assertFalse { c2.contains(appS1.key) }
    }

    @Test
    fun addScript() {
        val appS3 = appS1.copy(
            key = ScriptEntryKey("3")
        )
        val cont = container
        val c2 = cont.addOrOverwriteScript(appS3)
        assertEquals(appS3, c2.getAppScript(appS3.key))

        val eb13 = ScriptEntry(
            key = ScriptEntryKey(
                wbKey = wbkey1,
                name = "3",
            ),
            script = "script_b13"
        )
        assertNull(cont.getScript(eb13.key))
        val c3 = cont.addOrOverwriteScript(eb13)
        assertEquals(eb13, c3.getScript(eb13.key))
    }

    @Test
    fun getScript() {
        assertEquals(eb11, container.getScript(eb11.key))
        assertEquals(eb11, container.getScript(eb11.key))
        assertEquals(eb22, container.getScript(eb22.key))
        assertEquals(appS1, container.getScript(appS1.key))
    }

    @Test
    fun getWbScript() {
        assertEquals(eb11, container.getWbScript(eb11.key))
        assertEquals(eb11, container.getWbScript(eb11.key.wbKey!!, eb11.key.name))
        assertEquals(eb22, container.getWbScript(eb22.key))
        assertNull(container.getWbScript(appS1.key))
    }

    @Test
    fun getWbScripts() {
        assertEquals(listOf(eb11, eb12), container.getScriptsOfWb(this.wbkey1).toList())
    }

    @Test
    fun addWbScript() {
        val eb23 = ScriptEntry(
            key = ScriptEntryKey(
                wbKey = wbkey2,
                name = "3",
            ),
            script = "script_b23"
        )
        assertNull(container.getWbScript(eb23.key))
        val c2 = container.addWbScript(eb23)
        assertEquals(eb23, c2.getWbScript(eb23.key))

        val eb22New = eb22.copy(script = "eb22New")
        val c3 = container.addOrOverwriteScript(eb22New)
        assertEquals(eb22New, c3.getScript(eb22.key))
    }

    @Test
    fun getAppScript() {
        assertEquals(appS1, container.getAppScript(appS1.key))
        assertNull(container.getAppScript(eb11.key))
    }

    @Test
    fun allWbScript() {
        assertTrue(TestUtils.compare2ListIgnoreOrder(listOf(eb11, eb12, eb21, eb22), container.allWbScripts))
    }

    @Test
    fun allAppScript() {
        assertTrue(
            TestUtils.compare2ListIgnoreOrder(
                listOf(appS1, appS2), container.allAppScripts
            )
        )
    }

    @Test
    fun allScript() {
        assertTrue {
            TestUtils.compare2ListIgnoreOrder(
                listOf(eb11, eb12, eb21, eb22, appS2, appS1),
                container.allScripts
            )
        }
    }
}
