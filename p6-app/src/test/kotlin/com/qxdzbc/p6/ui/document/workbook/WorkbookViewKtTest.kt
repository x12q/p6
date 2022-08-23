package com.qxdzbc.p6.ui.document.workbook

import androidx.compose.ui.test.junit4.createComposeRule
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.StateUtils.rms
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock


internal class WorkbookViewKtTest {
    @get:Rule
    val cr = createComposeRule()

    @Test
    fun z() {

        cr.setContent {
            val wb = rms {
                WorkbookImp(
                    keyMs = WorkbookKey("", null).toMs(),
                ).addMultiSheetOrOverwrite(
                    listOf(
                        WorksheetImp("sheet1".toMs(), mock()),
                        WorksheetImp("sheet2".toMs(),mock())
                    )
                )
            }
        }
    }
}
