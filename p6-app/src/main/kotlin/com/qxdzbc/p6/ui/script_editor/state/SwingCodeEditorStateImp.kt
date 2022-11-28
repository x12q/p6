package com.qxdzbc.p6.ui.script_editor.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.document.script.ScriptEntry
import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.ErrorContainer
import com.qxdzbc.p6.app.oddity.ErrorContainerImp
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.ui.script_editor.script_tree.state.ScriptTreeState
import com.qxdzbc.p6.ui.script_editor.script_tree.state.ScriptTreeStateImp
import com.qxdzbc.p6.ui.script_editor.script_tree.state.TreeNodeStateContainer
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

/**
 * This class is used with the swing code editor to avoid unnecessary update
 */
data class SwingCodeEditorStateImp(
    private val wbContMs:Ms<WorkbookContainer>,
    override val centralScriptContainerMs: Ms<CentralScriptContainer>,
    private val outputListMs: Ms<List<String>> = ms(emptyList()),
    override val errorContainerMs: Ms<ErrorContainer> = ms(ErrorContainerImp()),
    override val openedScriptsMs: Ms<Set<ScriptEntryKey>> = ms(emptySet()),
    override val scriptTreeStateMs: Ms<ScriptTreeState> = ms(ScriptTreeStateImp.fromCodeContainer(centralScriptContainerMs,{wbContMs.value.containWb(it)})),
    override val currentCodeKey: ScriptEntryKey? = null,
    override val renameScriptDialogIsOpen: Boolean = false,
    override val renameTarget: ScriptEntryKey? = null,
) : CodeEditorState {

    override var scriptTreeState: ScriptTreeState by scriptTreeStateMs
    override var centralScriptContainer: CentralScriptContainer by centralScriptContainerMs

    override val treeNodeStateContainerMs: Ms<TreeNodeStateContainer> get() = this.scriptTreeState.nodeStateContainerMs
    override var treeNodeStateContainer: TreeNodeStateContainer by this.scriptTreeState.nodeStateContainerMs

    override var openedScripts: Set<ScriptEntryKey> by openedScriptsMs
    override fun alreadyOpen(key: ScriptEntryKey): Boolean {
        return key in this.openedScripts
    }

    override fun removeOpenScript(key: ScriptEntryKey): CodeEditorState {
        openedScripts = openedScripts - key
        return this
    }

    override fun openScript(scriptKey: ScriptEntryKey): CodeEditorState {
        if (this.centralScriptContainer.contains(scriptKey)) {
            this.openedScripts = this.openedScripts + scriptKey
        }
        return this
    }

    override var errorContainer: ErrorContainer by errorContainerMs
    override val currentScript: String? get() = this.currentScriptEntry?.script

    override fun updateCurrentScript(newCode: String): CodeEditorState {
        val newScript = this.currentScriptEntry?.copy(
            script = newCode
        )
        newScript?.also {
            this.centralScriptContainer = this.centralScriptContainer.addOrOverwriteScript(it)
        }
        return this
    }

    override fun removeScriptOfWb(workbookKey: WorkbookKey): CodeEditorState {
        this.scriptTreeState = this.scriptTreeState.removeWbNode(workbookKey)
        this.centralScriptContainer = this.centralScriptContainer.removeScriptOfWb(workbookKey)
        return this
    }

    override fun replaceWbKey(oldWbKey: WorkbookKey, newWbKey: WorkbookKey): CodeEditorState {
        this.scriptTreeState.treeNodeStateCont =
            this.scriptTreeState.treeNodeStateCont.replaceWorkbookKey(oldWbKey, newWbKey)
        val currentScriptKey = this.currentCodeKey
        if (currentScriptKey != null && oldWbKey == currentScriptKey.wbKey) {
            return this.copy(currentCodeKey = currentScriptKey.copy(wbKey = newWbKey))
        }
        return this
    }

    /**
     * replace script key in:
     *  - script container
     *  - node state container
     *  - opened script list
     *  - current script tab
     */
    override fun replaceScriptKey(
        oldKey: ScriptEntryKey,
        newKey: ScriptEntryKey
    ): Result<CodeEditorState, ErrorReport> {
        val scriptCont by this.centralScriptContainerMs
        val stateCont by this.treeNodeStateContainerMs
        var newState: CodeEditorState = this
        when (val replaceRs = scriptCont.replaceScriptKeyRs(oldKey, newKey)) {
            is Ok -> {
                when (val replaceStateRs = stateCont.replaceScriptKey(oldKey, newKey)) {
                    is Ok -> {
                        this.centralScriptContainer = replaceRs.value
                        this.treeNodeStateContainer = replaceStateRs.value
                        // x: update the opened script if need
                        this.openedScripts = run {
                            val mOs = this.openedScripts.toMutableList()
                            val index = mOs.indexOf(oldKey)
                            if (index >= 0) {
                                mOs[index] = newKey
                            }
                            mOs.toSet()
                        }
                        // x: update the current script if need
                        if (this.currentCodeKey == oldKey) {
                            newState = this.setCurrentScriptKey(newKey)
                        }
                        return Ok(newState)
                    }
                    is Err -> {
                        return replaceStateRs
                    }
                }
            }
            is Err -> {
                return replaceRs
            }
        }
    }

    override fun setRenameTarget(key: ScriptEntryKey): CodeEditorState {
        return this.copy(renameTarget = key)
    }

    override fun openRenameScriptDialog(): CodeEditorState {
        return this.copy(renameScriptDialogIsOpen = true)
    }

    override fun closeRenameScriptDialog(): CodeEditorState {
        return this.copy(renameScriptDialogIsOpen = false)
    }

    override val hasOpenedCodes: Boolean
        get() = this.openedScripts.isNotEmpty()

    override fun setCurrentScriptKey(scriptKey: ScriptEntryKey?): CodeEditorState {
        return this.copy(currentCodeKey = scriptKey)
    }

    override val currentScriptEntry: ScriptEntry?
        get() = this.centralScriptContainer.getScript(this.currentCodeKey)

    override val outputList: List<String> get() = outputListMs.value
    override val output: String
        get() {
            val rt = outputList.reversed().joinToString(System.lineSeparator())
            return rt
        }

    override fun addOutput(output: String): CodeEditorState {
        outputListMs.value = outputList + output
        return this
    }

    override fun clearOutput(): CodeEditorState {
        outputListMs.value = emptyList()
        return this
    }
}
