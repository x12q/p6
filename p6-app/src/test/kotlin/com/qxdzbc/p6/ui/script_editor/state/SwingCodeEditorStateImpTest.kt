package com.qxdzbc.p6.ui.script_editor.state

import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.script.ScriptContainer
import com.qxdzbc.p6.app.document.script.ScriptContainerImp
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import org.mockito.kotlin.mock
import test.TestSample
import kotlin.test.*

class SwingCodeEditorStateImpTest{
    lateinit var state:SwingCodeEditorStateImp
    lateinit var codeCont: CentralScriptContainer
    lateinit var testSample: TestSample
    @BeforeTest
    fun b(){
        testSample = TestSample()
        codeCont =testSample.sampleCodeContainer
        val wbContMs:Ms<WorkbookContainer> = testSample.appState.globalWbContMs
        val appScriptContMs:Ms<ScriptContainer> = ms(ScriptContainerImp())
        state = SwingCodeEditorStateImp(
            wbContMs = wbContMs,
            centralScriptContainerMs = testSample.sampleCodeContainerMs,
            currentCodeKey = null,
        )
    }

    @Test
    fun currentScript(){
        val cKey = codeCont.allWbScripts[0].key
        assertNull(state.currentScriptEntry)
        assertNull(state.currentCodeKey)
        val s2 = state.setCurrentScriptKey(cKey)
        assertEquals(cKey, s2.currentCodeKey)
        assertEquals(codeCont.getScript(cKey), s2.currentScriptEntry)
    }

    @Test
    fun updateCurrentScript(){
        val cKey = codeCont.allWbScripts[0].key
        val s2 = state.setCurrentScriptKey(cKey)
        assertEquals(codeCont.getScript(cKey)?.script, s2.currentScript)

        val s3 = s2.updateCurrentScript("new code 123")
        assertEquals("new code 123", s3.currentScript)
        assertEquals("new code 123", s3.currentScriptEntry?.script)
    }

    @Test
    fun openRenameScriptDialog_closeRenameScriptDialog(){
        assertFalse { this.state.renameScriptDialogIsOpen }
        val s = this.state.openRenameScriptDialog()
        assertTrue { s.renameScriptDialogIsOpen }
        val s2 = s.closeRenameScriptDialog()
        assertFalse { s2.renameScriptDialogIsOpen }
    }

    @Test
    fun setRenameTarget(){
        val rt:ScriptEntryKey = mock()
        assertNotEquals(rt,this.state.renameTarget)
        val s = this.state.setRenameTarget(rt)
        assertEquals(rt, s.renameTarget)
    }
}
