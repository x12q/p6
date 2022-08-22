package com.qxdzbc.p6.app.communication.applier.script.new_script

import com.qxdzbc.p6.app.action.script.new_script.applier.NewScriptApplierImp
import com.qxdzbc.p6.app.action.common_data_structure.ErrorIndicator
import com.qxdzbc.p6.app.action.script.new_script.NewScriptNotification
import com.qxdzbc.p6.app.document.script.ScriptEntry
import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.script_editor.ScriptEditorErrorRouter
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
import com.qxdzbc.p6.ui.script_editor.state.SwingCodeEditorStateImp
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import test.TestSample
import kotlin.test.*

class NewScriptApplierImpTest {

    lateinit var applier: NewScriptApplierImp
    lateinit var ceErrRouter:ScriptEditorErrorRouter
    lateinit var stateMs:Ms<CodeEditorState>
    lateinit var scriptContMs:Ms<CentralScriptContainer>
    lateinit var wbContMs:Ms<WorkbookContainer>
    val codeCont:CentralScriptContainer get() = scriptContMs.value
    lateinit var testSample: TestSample
    @BeforeTest
    fun b(){
        testSample = TestSample()
        wbContMs = testSample.appState.globalWbContMs
        scriptContMs=testSample.appState.centralScriptContainerMs
        stateMs = ms(SwingCodeEditorStateImp(
            wbContMs = wbContMs,
            centralScriptContainerMs = scriptContMs
        ))
        ceErrRouter = mock()
        applier = NewScriptApplierImp(
            codeEditorStateMs = stateMs,
            ceErrRouter = ceErrRouter
        )
    }

    @Test
    fun `applyNewScript ok case, app script`() {
        val k = ScriptEntryKey("new app script")
        assertNull(scriptContMs.value.getScript(k))
        applier.applyNewScript(
            newScriptEntry = ScriptEntry(
                key = k,
                script="app script"
            ),
            errorIndicator = ErrorIndicator.noError
        )
        assertNotNull(scriptContMs.value.getScript(k))
    }

    @Test
    fun `applyNewScript ok case, wb script`() {
        val wbKey = wbContMs.value.wbList[0].key
        val k = ScriptEntryKey("new wb script", wbKey)
        assertNull(scriptContMs.value.getScript(k))
        applier.applyNewScript(
            newScriptEntry = ScriptEntry(
                key = k,
                script="wb script"
            ),
            errorIndicator = ErrorIndicator.noError
        )
        assertNotNull(scriptContMs.value.getScript(k))
    }

    @Test
    fun `applyNewScript error case`() {
        val wbKey = wbContMs.value.wbList[0].key
        val k = ScriptEntryKey("new wb script", wbKey)
        val errorReport = TestSample.sampleErrorReport
        assertNull(scriptContMs.value.getScript(k))
        applier.applyNewScript(
            newScriptEntry = ScriptEntry(
                key = k,
                script="wb script"
            ),
            errorIndicator = ErrorIndicator.error(errorReport)
        )
        assertNull(scriptContMs.value.getScript(k))
        verify(this.ceErrRouter, times(1)).toCodeEditorWindow(errorReport)
    }

    @Test
    fun `apply new script notification - ok case`(){
        val wbKey0 = wbContMs.value.wbList[0].key
        val wbKey1 = wbContMs.value.wbList[1].key
        val notif = NewScriptNotification(
            scriptEntries = listOf(
                ScriptEntry(
                    key = ScriptEntryKey("s1",wbKey0),
                ),
                ScriptEntry(
                    key = ScriptEntryKey("s1",wbKey1),
                ),
                ScriptEntry(
                    key = ScriptEntryKey("s1",null),
                )
            ),
            errorIndicator = ErrorIndicator.noError
        )
        assertNull( codeCont.getScript(notif.scriptEntries[0].key))
        assertNull( codeCont.getScript(notif.scriptEntries[1].key))
        assertNull( codeCont.getScript(notif.scriptEntries[2].key))
        applier.applyNewScriptNotif(notif)
        assertNotNull( codeCont.getScript(notif.scriptEntries[0].key))
        assertNotNull( codeCont.getScript(notif.scriptEntries[1].key))
        assertNotNull( codeCont.getScript(notif.scriptEntries[2].key))
    }

    @Test
    fun `apply new script notification - err case`(){
        val errorReport = TestSample.sampleErrorReport
        val notif = NewScriptNotification(
            scriptEntries = listOf(),
            errorIndicator = ErrorIndicator.error(errorReport)
        )
        applier.applyNewScriptNotif(notif)
        verify(this.ceErrRouter, times(1)).toCodeEditorWindow(errorReport)
    }
}
