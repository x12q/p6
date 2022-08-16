@file:OptIn(ExperimentalMaterialApi::class)

package com.emeraldblast.p6.ui.common.view.dialog

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import com.emeraldblast.p6.app.oddity.OddMsg
import com.emeraldblast.p6.common.exception.error.ErrorHeader
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.ui.common.compose.testApp
import com.emeraldblast.p6.ui.common.view.dialog.error.ErrorDialogWithStackTrace


fun main() = testApp {
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
