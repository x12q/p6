package com.emeraldblast.p6.ui.script_editor.script_tree

import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import com.emeraldblast.p6.ui.common.compose.LayoutCoorsUtils.ifAttachedComposable
import com.emeraldblast.p6.ui.common.compose.StateUtils.rms
import com.emeraldblast.p6.ui.common.compose.OffsetUtils.toIntOffset
import com.emeraldblast.p6.ui.common.view.MBox
import com.emeraldblast.p6.ui.common.view.tree_view.TreeNode2
import com.emeraldblast.p6.ui.script_editor.script_tree.action.ScriptTreeAction
import com.emeraldblast.p6.ui.script_editor.script_tree.state.ScriptTreeState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScriptTree(
    state: ScriptTreeState,
    action: ScriptTreeAction,
) {
    val scriptCont = state.centralScriptContainer
    val stateCont by state.nodeStateContainerMs
    TreeNode2(
        //root node
        stateMs = stateCont.getAppNodeStateMs(),
        nodeText = "P6",
        contextMenuItems = listOf(
            ContextMenuItem("New script") {
                action.addNewScript(null)
            }
        ),
        onClick = {
            action.clickOnAppNode()
        }
    ) {
        var l: LayoutCoordinates? by rms(null)
        MBox(modifier = Modifier.onGloballyPositioned {
            l = it
        }) {
            Column {
                for (wbk in scriptCont.allWbKey) {
                    val wbNodeStateMs = stateCont.getWbNodeStateMs(wbk)
                    if (wbNodeStateMs != null) {
                        TreeNode2(
                            stateMs = wbNodeStateMs,
                            nodeText = wbk.name,
                            contextMenuItems = listOf(
                                ContextMenuItem("New script") {
                                    action.addNewScript(wbk)
                                }
                            ),
                            onClick = {
                                action.clickOnWbNode(wbk)
                            }
                        ) {
                            val scriptOfWb = scriptCont.getScripts(wbk)
                            Column {
                                for ( script in scriptOfWb) {

                                    val scriptNodeState = stateCont.getNodeStateMs(script.key)
                                    if (scriptNodeState != null) {
                                        TreeNode2(
                                            stateMs = scriptNodeState,
                                            nodeText = script.name,
                                            onClick = {
                                                action.clickOnScriptItem(script.key)
                                            },
                                            onDoubleClick = {
                                                action.doubleClickOnItem(script.key)
                                            },
                                            contextMenuItems = listOf(
                                                ContextMenuItem("Remove") {
                                                    action.removeScript(script.key)
                                                },
                                                ContextMenuItem("Rename") {
                                                    action.openRenameScriptDialog(script.key)
                                                }
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                for (script in scriptCont.allAppScripts) {
                    val scriptNodeState = stateCont.getNodeStateMs(script.key)
                    if (scriptNodeState != null) {
                        TreeNode2(
                            stateMs = scriptNodeState,
                            nodeText = script.name,
                            onClick = {
                                action.clickOnScriptItem(script.key)
                            },
                            onDoubleClick = {
                                action.doubleClickOnItem(script.key)
                            },
                            contextMenuItems = listOf(
                                ContextMenuItem("Remove") {
                                    action.removeScript(script.key)
                                },
                                ContextMenuItem("Rename") {
                                    action.openRenameScriptDialog(script.key)
                                }
                            )
                        )
                    }
                }
            }
            val currentNodeState = state.currentNodeState
            if (currentNodeState != null) {
                val layout = currentNodeState.layoutCoorWrapperMs.value
                layout?.ifAttachedComposable {itemLayout->
                    l.ifAttachedComposable {
                        val wp = itemLayout.posInWindow
                        val p = it.windowToLocal(wp).toIntOffset()
                        MBox(
                            modifier = Modifier
                                .offset {p }
                                .size(itemLayout.size)
                                .background(Color.Blue.copy(alpha = 0.7F))
                        )
                    }
                }
            }
        }
    }
}

//fun main() {
//    testApp {
//        val wbkey1 = WorkbookKey("b1")
//        val wbkey2 = WorkbookKey("b2")
//        val eb11 = ScriptEntry(
//            key = ScriptEntryKey(
//                wbKey = wbkey1,
//                name = "1",
//            ),
//            script = "script_b11"
//        )
//        val eb12 = ScriptEntry(
//            key = ScriptEntryKey(
//                wbKey = wbkey1,
//                name = "2",
//            ),
//            script = "script_b12"
//        )
//        val eb21 = ScriptEntry(
//            key = ScriptEntryKey(
//                wbKey = wbkey2,
//                name = "1",
//            ),
//            script = "script_b21"
//        )
//        val eb22 = ScriptEntry(
//            key = ScriptEntryKey(
//                wbKey = wbkey2,
//                name = "2",
//            ),
//            script = "script_b22"
//        )
//        val appS1 = ScriptEntry(
//            key = ScriptEntryKey(
//                wbKey = null,
//                name = "1"
//            ),
//            script = "appS1"
//        )
//        val appS2 = ScriptEntry(
//            key = ScriptEntryKey(
//                wbKey = null,
//                name = "2"
//            ),
//            script = "appS2"
//        )
//        val container = CentralScriptContainerImp(
//            appScriptContainerMs = ms(ScriptContainerImp.fromScriptEntries(listOf(appS1,appS2))),
//            workbookContainerMs = ms(
//                WorkbookContainerImp(
//                    wbList = listOf(
//                        WorkbookImp(
//                            key = wbkey1,
//                            scriptContainer= ScriptContainerImp.fromScriptEntries(listOf(eb11, eb12))
//                        ),
//                        WorkbookImp(
//                            key = wbkey2,
//                            scriptContainer= ScriptContainerImp.fromScriptEntries(listOf(eb21, eb22))
//                        ),
//                    )
//                )
//            )
//        )
//        val state = ScriptTreeStateImp.fromCodeContainer(
//            checkWbExist={true},
//            centralScriptContainerMs = ms(container)
//        )
//        ScriptTree(
//            state = state,
//            action = ScriptTreeActionDoNothing()
//        )
//    }
//}
