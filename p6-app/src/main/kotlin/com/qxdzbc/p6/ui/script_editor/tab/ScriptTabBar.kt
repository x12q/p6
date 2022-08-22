package com.qxdzbc.p6.ui.script_editor.tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.document.script.ScriptEntry
import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.ui.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.compose.TestApp
import com.qxdzbc.p6.ui.common.view.tab.MTabRow
import com.qxdzbc.p6.ui.common.view.tab.Tabs
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorAction
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorActionDoNothing

@Composable
fun ScriptTabBar(
    currentKey:ScriptEntryKey?,
    openedScripts: Set<ScriptEntryKey>,
    codeEditorAction: CodeEditorAction,
) {
    if (openedScripts.isNotEmpty()) {
        var selectedIndex by rms(0)
        MTabRow(
            selectedIndex = selectedIndex,
            tabs = {
                for ((i, key) in openedScripts.withIndex()) {
                    val isSelected = key == currentKey
                    Tabs.MTabWithCloseButton(
                        isSelected = isSelected,
                        text = key.name,
                        onClick = {
                            selectedIndex = i
                            codeEditorAction.showCode(key)
                        },
                        onClose = {
                            codeEditorAction.closeCode(key)
                        },
                    )
                }
            }
        )
    }
}


fun main() {
    TestApp {
        ScriptTabBar(
            codeEditorAction = CodeEditorActionDoNothing(),
            openedScripts = setOf(
                ScriptEntry(
                    key = ScriptEntryKey("Scrip1"),
                    script = "abc"
                ).key,
                ScriptEntry(
                    key = ScriptEntryKey("Script2"),
                    script = "abc"
                ).key
            ),
            currentKey = ScriptEntryKey("Script2")
        )
    }
}
