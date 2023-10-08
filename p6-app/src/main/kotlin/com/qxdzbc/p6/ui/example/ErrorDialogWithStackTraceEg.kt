@file:OptIn(ExperimentalMaterialApi::class)

package com.qxdzbc.p6.ui.example


import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import com.qxdzbc.common.compose.view.testApp
import com.qxdzbc.p6.err.ErrMsg
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.SingleErrorReport

import com.qxdzbc.p6.ui.common.view.dialog.error.ErrorDialogWithStackTrace


fun main() = testApp {
    ErrorDialogWithStackTrace(
        errMsg = ErrMsg.Error(
            SingleErrorReport(
                header = ErrorHeader(
                    errorCode = "Code123",
//                    errorDescription = "QWE"
                    errorDescription = "QWEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEWWWWWWWWWWWWWWWWWWWWWWGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGAAAAAAAAAAAAAAAAAAAAAAAAAAAAASSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCZZZZZZZZZZZZZZZZZZ"
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
