package com.emeraldblast.p6.ui.document.workbook.sheet_tab

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.emeraldblast.p6.ui.document.workbook.sheet_tab.tab.SheetTabStateImp
import com.emeraldblast.p6.ui.document.workbook.sheet_tab.tab.SheetTabView
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SheetTabViewKtTest {
    @get:Rule
    val ctr = createComposeRule()

    @Test
    fun sheetTabView() {
        val state = SheetTabStateImp("sheet 1", true)
        var a = 0
        val tab = "tab"
        ctr.setContent {
            Box {
                SheetTabView(
                    state=state,
                    modifier = Modifier.testTag(tab),
                    onClick = { a += 1 }
                )
            }
        }
        ctr.onNodeWithTag(tab).assertTextEquals("sheet 1").performClick()
        assertEquals(1, a)
    }
}
