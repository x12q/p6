//package com.qxdzbc.p6.ui.script_editor.action
//
//import com.qxdzbc.p6.ui.script_editor.code_container.CodeContainer
//import com.qxdzbc.common.compose.Ms
//import com.qxdzbc.common.compose.StateUtils.ms
//import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
//import kotlinx.coroutines.GlobalScope
//import org.mockito.kotlin.mock
//import test.TestSample
//import kotlin.test.*
//
//class CodeEditorActionImpTest {
//    lateinit var action: CodeEditorActionImp
//    lateinit var stateMs: Ms<CodeEditorState>
//    lateinit var cont: CodeContainer
//    val state get() = stateMs.value
//
//    @BeforeTest
//    fun b() {
//        cont = TestSample.sampleCodeContainer
//        stateMs = ms(
//            SwingCodeEditorStateImp(
//                codeContainerMs = ms(cont)
//            )
//        )
//        action = CodeEditorActionImp(
//            codeEditorStateMs = stateMs,
//            codeRunner = mock(),
//            executionScope = GlobalScope,
//            ceErrRouter = mock()
//        )
//    }
//
//    @Test
//    fun openScript_closeScript() {
//        val target = TestSample.sampleCodeContainer.allWbScripts[0]
//        assertFalse { stateMs.value.openedScripts.contains(target.key) }
//        action.openCode(target.key)
//        assertTrue { stateMs.value.openedScripts.contains(target.key) }
//        action.closeCode(target.key)
//        assertFalse { stateMs.value.openedScripts.contains(target.key) }
//    }
//
//    @Test
//    fun showScript() {
//        val key = TestSample.sampleCodeContainer.allWbScripts[1].key
//        action.showCode(key)
//        assertEquals(key, stateMs.value.currentCodeKey)
//    }
//
//    @Test
//    fun closeRenameScriptDialog() {
//        stateMs.value = stateMs.value.openRenameScriptDialog()
//        assertTrue { stateMs.value.renameScriptDialogIsOpen }
//        action.closeRenameScriptDialog()
//        assertFalse { stateMs.value.renameScriptDialogIsOpen }
//    }
//
//    @Test
//    fun renameScript() {
//        val t1 = cont.allScripts[0]
//        val t2 = cont.allScripts[1]
//        val t3 = cont.allScripts[2]
//        stateMs.value = state.setCurrentScriptKey(t1.key)
//        stateMs.value.openedScriptsMs.value = state.openedScripts + t1.key + t2.key + t3.key
//
//        var t1Index = state.openedScripts.indexOf(t1.key)
//        var t2Index = state.openedScripts.indexOf(t2.key)
//        var t3Index = state.openedScripts.indexOf(t3.key)
//
//        val newName = "newName123"
//        val newKeyT1 = t1.key.copy(name = newName)
//        assertNotNull(state.codeContainer.getScript(t1.key))
//        assertNull(state.codeContainer.getScript(newKeyT1))
//        action.renameScript(t1.key, newName)
//
//        assertNotNull(state.codeContainer.getScript(newKeyT1))
//        assertNull(state.codeContainer.getScript(t1.key))
//        assertEquals(newKeyT1, state.currentCodeKey)
//        assertEquals(setOf(newKeyT1,t2.key,t3.key), state.openedScripts)
//
//        val newNameT2 = "newNameT2"
//        val newT2 = t2.key.copy(name = newNameT2)
//        action.renameScript(t2.key, newNameT2)
//        assertEquals(newKeyT1, state.currentCodeKey)
//        assertEquals(setOf(newKeyT1,newT2,t3.key), state.openedScripts)
//    }
//}
