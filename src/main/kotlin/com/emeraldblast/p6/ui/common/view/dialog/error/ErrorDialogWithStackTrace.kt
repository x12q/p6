package com.emeraldblast.p6.ui.common.view.dialog.error

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emeraldblast.p6.app.common.utils.Utils
import com.emeraldblast.p6.app.oddity.OddMsg
import com.emeraldblast.p6.ui.common.compose.StateUtils.rms
import com.emeraldblast.p6.ui.common.view.BorderBox
import com.emeraldblast.p6.ui.common.view.BorderStyle
import com.emeraldblast.p6.ui.common.view.BorderStyle.Companion.debugAll
import com.emeraldblast.p6.ui.common.view.MBox
import com.emeraldblast.p6.ui.common.view.dialog.ErrorDialogWithStackTraceState

/**
 * An error dialog that contains:
 * - the error message,
 * - a collapsable panel showing stack trace for the error
 */
@Composable
fun ErrorDialogWithStackTrace(
    oddMsg: OddMsg,
    errorTitle: String = oddMsg.type.name,
    onOkClick: () -> Unit,
    onDismiss: () -> Unit = onOkClick,
    modifier: Modifier = Modifier,
    initState: ErrorDialogWithStackTraceState = ErrorDialogWithStackTraceState.default
) {
    var state by rms { initState }
    val copied = state.copied
    val showStackTrace = state.showStackTrace
    OkErrorDialog(
        dialogState = state.dialogState,
        errorTitle = { Text(errorTitle) },
        modifier = modifier,
        resizable = false,
        errorBody = {
            Column {
                // x: message box
                BorderBox(
                    style = BorderStyle.NONE.debugAll(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(state.messageMaxHeight.dp)
                ) {
                    val ss = rememberScrollState(0)
                    Text(
                        text = oddMsg.msg,
                        modifier = Modifier
                            .verticalScroll(ss)
                            .align(Alignment.CenterStart)
                    )
                    VerticalScrollbar(
                        modifier = Modifier.align(Alignment.CenterEnd)
                            .fillMaxHeight(),
                        adapter = rememberScrollbarAdapter(ss)
                    )
                }
                // x: show/hide stack trace button
                BorderBox(
                    style = BorderStyle.NONE.debugAll(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MBox(modifier = Modifier.align(Alignment.CenterEnd).clickable {
                        state = state.switchShowStackTrace()

                    }) {
                        Icon(
                            imageVector = if(state.showStackTrace) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                            contentDescription = "Show/hide stack trace",
                            modifier = Modifier
                                .align(Alignment.Center),

                        )
                    }
                }
                if (showStackTrace) {
                    val stackTrace = oddMsg.errorReport.stackTraceStr()
                    BorderBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(state.stackTraceMaxHeight.dp)
                    ) {
                        Column(modifier = Modifier.padding(5.dp)) {
                            MBox(modifier = Modifier.fillMaxWidth()) {
                                Text("Stack trace:", modifier = Modifier.align(Alignment.CenterStart))
                                BorderBox(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .clickable {
                                            if (!copied) {
                                                state = state.setCopied(true)
                                            }
                                            Utils.copyTextToClipboard(stackTrace)
                                        }
                                        .padding(3.dp)
                                )
                                {
                                    if (copied) {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = "Copy stack trace",
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(3.dp)
                                        )
                                    } else {
                                        Icon(
                                            Icons.Filled.ContentCopy,
                                            contentDescription = "Copy stack trace",
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(3.dp)
                                        )
                                    }
                                }
                            }
                            Divider()
                            MBox {
                                val scrollState = rememberScrollState(0)
                                BasicText(
                                    text = stackTrace,
                                    modifier = Modifier.verticalScroll(scrollState)
                                )
                                VerticalScrollbar(
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                        .fillMaxHeight(),
                                    adapter = rememberScrollbarAdapter(scrollState)
                                )
                            }
                        }
                    }
                }
            }
        },
        onOkClick = onOkClick,
        onDismiss = onDismiss,
    )
}
