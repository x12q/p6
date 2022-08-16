package com.emeraldblast.p6.ui.script_editor.state

//data class CodeEditorStateImp(
//    override val code: String = "",
//    override val outputList: List<String> = emptyList(),
//    override val oddityContainerMs: MutableState<OddityContainer> = ms(OddityContainerImp()),
//    override val openedScriptsMs: Ms<List<ScriptEntry>> = ms(emptyList()),
//    override val codeContainerMs: Ms<CodeContainer> = ms(CodeContainerImp()),
//    override val codeTreeStateMs: Ms<CodeTreeState> = ms(CodeTreeStateImp(codeContainerMs)),
//) : CodeEditorState {
//
//    override var codeTreeState: CodeTreeState by codeTreeStateMs
//    override var codeContainer: CodeContainer by codeContainerMs
//    override var openedScripts: List<ScriptEntry> by openedScriptsMs
//    override val output: String
//        get() {
//            val rt = outputList.reversed().joinToString("\n")
//            return rt
//        }
//
//    override var oddityContainer: OddityContainer by oddityContainerMs
//
//    override fun setCurrentCode(code: String): CodeEditorState {
//        return this.copy(code = code)
//    }
//
//    override fun addOutput(output: String): CodeEditorState {
//        return this.copy(outputList = outputList + output)
//    }
//
//    override fun clearOutput(): CodeEditorState {
//        return this.copy(outputList = emptyList())
//    }
//}
