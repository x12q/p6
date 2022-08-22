@file:OptIn(ExperimentalMaterialApi::class)

package com.qxdzbc.p6.ui.common.view.dialog

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import com.qxdzbc.p6.app.oddity.OddMsg
import com.qxdzbc.p6.common.exception.error.ErrorHeader
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.ui.common.compose.TestApp
import com.qxdzbc.p6.ui.common.view.dialog.error.ErrorDialogWithStackTrace


fun main() = TestApp {
    ErrorDialogWithStackTrace(
        oddMsg = OddMsg.Error(
            ErrorReport(
                header = ErrorHeader(
                    errorCode = "Code123",
                    errorDescription = "QWE"
//                    errorDescription = "QWEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEWWWWWWWWWWWWWWWWWWWWWWGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGAAAAAAAAAAAAAAAAAAAAAAAAAAAAASSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCZZZZZZZZZZZZZZZZZZ"
                )
            )
        ),
        onOkClick = {
            this.exitApplication()
        },
        onDismiss = { this.exitApplication() },
        modifier = Modifier,
    )
}
