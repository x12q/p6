package com.emeraldblast.p6.ui.app.cell_editor.range_selector

//class RangeSelectorActionImp(
//    @AppStateMs
//    val appStateMs: Ms<AppState>
//) : RangeSelectorAction {
//
//    var appState by appStateMs
//
//    @OptIn(ExperimentalComposeUiApi::class)
//    override fun handleKeyboardEvent(
//        keyEvent: KeyEvent,
//        state: RangeSelectorState
//    ): Boolean {
//        return false
////        val cursorState = state
////        val wbKey = cursorState.id.wbKey
////        val wsName = cursorState.id.wsName
////        if (wbKey != null && wsName != null) {
////            val wsState = appState.getWsState(wbKey, wsName)
////            if (wsState != null) {
////                return if (keyEvent.type == KeyEventType.KeyDown) {
////                    if (keyEvent.isCtrlPressedAlone) {
////                        handleKeyWithCtrlDown(keyEvent, cursorState)
////                    } else if (keyEvent.isShiftPressedAlone) {
////                        handleKeyboardEventWhenShiftDown(keyEvent, cursorState)
////                    } else if (keyEvent.isCtrlShiftPressed) {
////                        handleKeyWithCtrlShift(keyEvent, wsState)
////                    } else {
////
////                        val wsStateMs =
////                            appState.getWsStateMs(wbKey, wsName)
////                        val cursorStateMs =
////                            appState.getCursorStateMs(wbKey, wsName)
////                        val colRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.colRulerStateMs
////                        val rowRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.rowRulerStateMs
////
////                        if (wsStateMs != null && cursorStateMs != null && colRulerStateMs != null && rowRulerStateMs != null) {
////                            when (keyEvent.key) {
////                                Key.Escape -> {
////                                    // TODO close the cursor
////                                    true
////                                }
////
////                                Key.DirectionUp -> {
////                                    up(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                                    true
////                                }
////                                Key.DirectionDown -> {
////                                    down(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                                    true
////                                }
////                                Key.DirectionLeft -> {
////                                    left(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                                    true
////                                }
////                                Key.DirectionRight -> {
////                                    right(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                                    true
////                                }
////                                Key.Home -> {
////                                    this.home(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                                    true
////                                }
////                                Key.MoveEnd -> {
////                                    this.end(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                                    true
////                                }
////                                else -> false
////                            }
////                        } else {
////                            false
////                        }
////                    }
////                } else {
////                    false
////                }
////
////            } else {
////                return false
////            }
////        } else {
////            return false
////        }
//    }
//
////    private fun handleKeyboardEventWhenShiftDown(
////        keyEvent: KeyEvent,
////        cursorState: RangeSelectorState,
////    ): Boolean {
////        if (keyEvent.isShiftPressedAlone) {
////
////            val wsStateMs = appState.getWsStateMs(cursorState.id.wbKey, cursorState.id.wsName)
////            val cursorStateMs = appState.getCursorStateMs(cursorState.id.wbKey, cursorState.id.wsName)
////            val colRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.colRulerStateMs
////            val rowRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.rowRulerStateMs
////            if (wsStateMs != null && cursorStateMs != null && colRulerStateMs != null && rowRulerStateMs != null) {
////                return when (keyEvent.key) {
////                    Key.DirectionUp -> {
////                        shiftUp(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                        true
////                    }
////                    Key.DirectionDown -> {
////                        shiftDown(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                        true
////                    }
////                    Key.DirectionLeft -> {
////                        shiftLeft(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                        true
////                    }
////                    Key.DirectionRight -> {
////                        shiftRight(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                        true
////                    }
////                    Key.Spacebar -> {
////                        shiftSpace(cursorStateMs, wsStateMs.value)
////                        true
////                    }
////                    else -> false
////                }
////
////            } else {
////                return false
////            }
////        } else {
////            return false
////        }
////    }
////
////    private fun handleKeyWithCtrlShift(keyEvent: KeyEvent, wsState: WorksheetState): Boolean {
////        if (keyEvent.isCtrlShiftPressed) {
////            return when (keyEvent.key) {
////                Key.DirectionUp -> {
////                    ctrlShiftUp(wsState)
////                    true
////                }
////                Key.DirectionDown -> {
////                    ctrlShiftDown(wsState)
////                    true
////                }
////                Key.DirectionLeft -> {
////                    ctrlShiftLeft(wsState)
////                    true
////                }
////                Key.DirectionRight -> {
////                    ctrlShiftRight(wsState)
////                    true
////                }
////                else -> false
////            }
////        } else {
////            return false
////        }
////    }
////
////    private fun handleKeyWithCtrlDown(keyEvent: KeyEvent, cursorState: RangeSelectorState): Boolean {
////        if (keyEvent.isCtrlPressedAlone) {
////            val wsStateMs = appState.getWsStateMs(cursorState.id.wbKey, cursorState.id.wsName)
////            val cursorStateMs = appState.getCursorStateMs(cursorState.id.wbKey, cursorState.id.wsName)
////            val colRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.colRulerStateMs
////            val rowRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.rowRulerStateMs
////            if (wsStateMs != null && cursorStateMs != null && colRulerStateMs != null && rowRulerStateMs != null) {
////                return when (keyEvent.key) {
////                    Key.V -> {
////                        pasteRange(wsStateMs.value.cursorState)
////                        true
////                    }
////                    Key.C -> {
////                        rangeToClipboard2(cursorState)
////                        true
////                    }
////                    Key.Z -> {
////                        undo(cursorState)
////                        true
////                    }
////                    Key.DirectionUp -> {
////                        ctrlUp(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                        true
////                    }
////                    Key.DirectionDown -> {
////                        ctrlDown(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                        true
////                    }
////                    Key.DirectionRight -> {
////                        ctrlRight(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                        true
////                    }
////                    Key.DirectionLeft -> {
////                        ctrlLeft(cursorStateMs, wsStateMs, colRulerStateMs, rowRulerStateMs)
////                        true
////                    }
////                    Key.Spacebar -> {
////                        ctrlSpace(wsStateMs.value)
////                        true
////                    }
////                    else -> false
////                }
////
////            } else {
////                return false
////            }
////        } else {
////            return false
////        }
////    }
//
//}
