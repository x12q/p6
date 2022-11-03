package com.qxdzbc.p6.ui.script_editor.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.coderunner.CodeRunner
import com.qxdzbc.p6.app.action.script.ScriptApplier
import com.qxdzbc.p6.app.action.script.ScriptRM
import com.qxdzbc.p6.app.action.script.new_script.NewScriptRequest

import com.qxdzbc.p6.app.document.script.ScriptEntry
import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.script_editor.ScriptEditorErrorRouter
import com.qxdzbc.p6.ui.script_editor.ScriptEditorErrors
import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class CodeEditorActionImp @Inject constructor(
    private val codeEditorStateMs: Ms<CodeEditorState>,
    private val codeRunner: CodeRunner,
    @com.qxdzbc.p6.di.AppCoroutineScope private val executionScope: CoroutineScope,
    private val ceErrRouter: ScriptEditorErrorRouter,
    private val scriptRM: ScriptRM,
    private val scriptApplier: ScriptApplier,
) : CodeEditorAction {
    var codeEditorState by codeEditorStateMs
    private val namePrefix = "Script"
    private val codeNamePattern = Pattern.compile("${namePrefix}(([1-9]+[0-9]*)|[0-9])")
    override fun runCode(code: String) {
        executionScope.launch(Dispatchers.Main) {
            val rs = codeRunner.run(code)
            rs.onSuccess {
                codeEditorState = codeEditorState.addOutput(it)
            }.onFailure {
                ceErrRouter.toOutputPanel(it)
            }
        }
    }

    override fun updateCurrentCode(code: String) {
        codeEditorState = codeEditorState.updateCurrentScript(code)

    }

    override fun openCode(key: ScriptEntryKey) {
        val entry = this.codeEditorState.centralScriptContainer.getScript(key)
        if (entry != null) {
            if (key !in codeEditorState.openedScripts) {
                codeEditorState.openedScripts = codeEditorState.openedScripts + key
            }
        }
    }

    override fun closeCode(key: ScriptEntryKey) {
        val entry = this.codeEditorState.centralScriptContainer.getScript(key)
        if (entry != null) {
            val closeScriptIsCurrentlySelected = key == this.codeEditorState.currentCodeKey
            if (closeScriptIsCurrentlySelected) {
                this.selectTheClosestKey(key)
            }
        }
        codeEditorState = codeEditorState.removeOpenScript(key)
    }

    override fun selectTheClosestKey(currentKey: ScriptEntryKey) {
        val openedScripts = this.codeEditorState.openedScripts
        if (currentKey in openedScripts) {
            if (openedScripts.size == 1) {
                return
            } else {
                val closestIndex = kotlin.run {
                    val currentIndex = openedScripts.indexOf(currentKey)
                    val prior = currentIndex - 1
                    val latter = currentIndex + 1
                    if (prior in openedScripts.indices) {
                        prior
                    } else if (latter in openedScripts.indices) {
                        latter
                    } else {
                        null
                    }
                }
                val closestKey = closestIndex?.let {
                    openedScripts.toList()[it]
                }
                if (closestKey != null) {
                    this.setCurrentCodeKey(closestKey)
                }
            }
        }
    }

    override fun closeRenameScriptDialog() {
        this.codeEditorState = this.codeEditorState.closeRenameScriptDialog()
    }

    override fun renameScript(key: ScriptEntryKey, newName: String) {
        val newKey = key.copy(name = newName)
        if (newName.isEmpty()) {
            this.ceErrRouter.toCodeEditorWindow(ScriptEditorErrors.EmptyScriptName())
        } else {
            when (val replaceRs = this.codeEditorState.replaceScriptKey(key, newKey)) {
                is Ok -> {
                    this.codeEditorState = replaceRs.value
                    this.closeRenameScriptDialog()
                }
                is Err -> {
                    this.closeRenameScriptDialog()
                    this.ceErrRouter.toCodeEditorWindow(replaceRs.error)
                }
            }
        }
    }

    override fun showCode(key: ScriptEntryKey) {
        this.setCurrentCodeKey(key)
    }

    override fun setCurrentCodeKey(key: ScriptEntryKey) {
        val entry = this.codeEditorState.centralScriptContainer.getScript(key)
        if (entry != null) {
            codeEditorState = codeEditorState.setCurrentScriptKey(entry.key)
        }
    }

    override fun addNewScript(workbookKey: WorkbookKey?) {
        val newName = run {
            val scripts = codeEditorState.centralScriptContainer.getScripts(workbookKey).map { it.name }
            val matchNamed = scripts.filter { this.codeNamePattern.matcher(it).matches() }
            val nextIndex = (matchNamed.maxOfOrNull { it.substring(namePrefix.length).toInt() } ?: 0) + 1
            "${namePrefix}${nextIndex}"
        }
        val newScriptEntry = ScriptEntry(
            key = ScriptEntryKey(
                name = newName,
                wbKey = workbookKey
            ),
            script = ""
        )

        val req = NewScriptRequest(newScriptEntry)
        val o = scriptRM.newScript(req)
        scriptApplier.applyNewScript(o)

    }
}
