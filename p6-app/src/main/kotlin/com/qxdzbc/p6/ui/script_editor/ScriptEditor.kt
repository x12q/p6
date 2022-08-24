package com.qxdzbc.p6.ui.script_editor

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.app.oddity.OddMsg
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorActionTable
import com.qxdzbc.p6.ui.common.R
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.view.dialog.Dialogs
import com.qxdzbc.p6.ui.common.view.dialog.error.ErrorDialogWithStackTrace
import com.qxdzbc.p6.ui.common.view.resizable_box.ResizableBox
import com.qxdzbc.p6.ui.common.view.resizable_box.ResizableBoxState
import com.qxdzbc.p6.ui.common.view.resizable_box.ResizeStyle
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorAction
import com.qxdzbc.p6.ui.script_editor.script_tree.ScriptTree
import com.qxdzbc.p6.ui.script_editor.script_tree.action.ScriptTreeAction
import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
import com.qxdzbc.p6.ui.script_editor.tab.ScriptTabBar
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JPanel
import javax.swing.KeyStroke
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener


@Composable
fun ScriptEditor(
    state: CodeEditorState,
    actionTable: CodeEditorActionTable,
) {
    var textArea: RSyntaxTextArea? by rms(null)
    val action: CodeEditorAction = actionTable.codeEditorAction
    val scriptTreeAction: ScriptTreeAction = actionTable.scriptTreeAction
    MBox(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row {
            BorderBox(
                style = BorderStyle.RIGHT,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                ResizableBox(
                    style = ResizeStyle.RIGHT,
                    initState = ResizableBoxState.default.copy(widthMs = ms(200.dp)),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    val hs = rememberScrollState(0)
                    val vs = rememberScrollState(0)
                    MBox(
                        modifier=Modifier
                            .fillMaxSize()
                            .horizontalScroll(hs)
                            .verticalScroll(vs)

                    ){
                        ScriptTree(
                            state = state.scriptTreeState,
                            action = scriptTreeAction,
                        )
                    }
                }
            }
            Column {
                BorderBox(style = BorderStyle.BOT, modifier = R.composite.mod.stdBar) {
                    Row {
                        Button(onClick = {
                            textArea?.text?.also {
                                action.runCode(it)
                            }
                        }) {
                            BasicText("Run")
                        }
                    }
                }

                BorderBox(style = BorderStyle.BOT) {
                    ScriptTabBar(
                        openedScripts = state.openedScripts,
                        codeEditorAction = action,
                        currentKey = state.currentCodeKey
                    )
                }

                BorderBox(style = BorderStyle.BOT, modifier = Modifier.weight(0.7F).fillMaxWidth()) {
                    if (state.hasOpenedCodes) {
                        SwingPanel(
                            background = Color.White,
                            modifier = Modifier.fillMaxSize(),
                            factory = {
                                val jpanel = JPanel(BorderLayout())
                                if (textArea == null) {
                                    textArea = RSyntaxTextArea(20, 60)
                                    textArea!!.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_PYTHON
                                    textArea!!.isCodeFoldingEnabled = true
                                    textArea!!.inputMap.put(
                                        KeyStroke.getKeyStroke("TAB"),
                                        "tab4Space"
                                    )
                                    textArea!!.actionMap.put("tab4Space", object : AbstractAction() {
                                        override fun actionPerformed(e: ActionEvent?) {
                                            val oldText = textArea!!.text
                                            val newText = "$oldText    "  //add 4 space
                                            textArea!!.text = newText
                                        }
                                    })
                                    textArea!!.document.addDocumentListener(object : DocumentListener {
                                        override fun insertUpdate(e: DocumentEvent?) {
                                        }

                                        override fun removeUpdate(e: DocumentEvent?) {
                                        }

                                        override fun changedUpdate(e: DocumentEvent?) {
                                            if (e != null) {
                                                val doc = e.document
                                                val newCode = (doc.getText(0, e.document.length))
                                                action.updateCurrentCode(newCode)
                                            }
                                        }
                                    })
                                }
                                textArea!!.text = state.currentScript
                                val scrollPane = RTextScrollPane(textArea)
                                jpanel.add(scrollPane)
                                jpanel
                            },
                        )

                    }
                }
                BorderBox(
                    style = BorderStyle.NONE, modifier = Modifier.fillMaxWidth()
                ) {
                    ResizableBox(
                        style = ResizeStyle.TOP,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BasicTextField(
                            value = state.output,
                            onValueChange = {},
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
        val rntg = state.renameTarget
        if (state.renameScriptDialogIsOpen && rntg != null) {
            Dialogs.RenameDialog(
                title = "Rename script",
                initName = rntg.name,
                onOk = { newName ->
                    action.renameScript(rntg, newName)
                },
                onCancel = {
                    action.closeRenameScriptDialog()
                }
            )
        }
        val oddityContainerMs = state.oddityContainerMs
        if (state.oddityContainer.isNotEmpty()) {
            for (oddMsg: OddMsg in oddityContainerMs.value.oddList) {
                ErrorDialogWithStackTrace(
                    oddMsg = oddMsg,
                    onOkClick = {
                        oddityContainerMs.value = oddityContainerMs.value.remove(oddMsg)
                    },
                )
            }
        }
    }
}

//fun main() {
//    testApp {
//        val wbContMs:Ms<WorkbookContainer> = ms(
//            WorkbookContainerImp(listOf(
//                WorkbookImp(
//                    key = WorkbookKey("wb1")
//                )
//            ))
//        )
//
//        val stateMs: Ms<CodeEditorState> = rms(
//            SwingCodeEditorStateImp(
//                wbContMs=wbContMs,
//                centralScriptContainerMs = ms(
//                    CentralScriptContainerImp(
//                        appScriptContainerMs = ms(
//                            ScriptContainerImp.fromScriptEntries(
//                            (1 .. 50).map{
//                                ScriptEntry(ScriptEntryKey("s${it}: ${"q".repeat(it)}"), "script ${it} content: ${
//                                    "q".repeat(it)
//                                }")
//                            }
//                        )),
//                        workbookContainerMs = wbContMs
//                    )
//                )
//            )
//        )
//        val state by stateMs
//
//        val actionTable = object : CodeEditorActionTable {
//            override val codeEditorAction: CodeEditorAction = CodeEditorActionImp(
//                codeEditorStateMs = stateMs,
//                codeRunner = FakeCodeRunner(),
//                executionScope = GlobalScope,
//                ceErrRouter = ScriptEditorErrorRouterImp(
//                    codeEditorStateMs = stateMs,
//                    errorRouter = ErrorRouterDoNothing()
//                ),
//                scriptRM = FakeScriptRM(),
//                scriptApplier = FakeScriptApplierImp()
//            )
//            override val scriptTreeAction: ScriptTreeAction = ScriptTreeActionImp(
//                codeEditorAction = codeEditorAction,
//                codeEditorStateMs = stateMs
//            )
//        }
//        ScriptEditor(
//            state = state,
//            actionTable = actionTable
//        )
//    }
//}
