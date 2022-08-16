package com.emeraldblast.p6.ui.common.view

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals


internal class OkCancelKtTest{
    @get:Rule
    val cr = createComposeRule()
    @Test
    fun testEvent(){
        var cancel = 0
        var ok = 0
        cr.setContent {
            Box{
                OkCancel(
                    okModifier = Modifier.testTag("Ok"),
                    cancelModifier = Modifier.testTag("Cancel"),
                    onCancel = {
                        cancel+=1
                    },
                    onOk = {
                        ok+=1
                    }
                )
            }
        }
        cr.onNodeWithTag("Ok").performClick()
        assertEquals(1,ok)
        cr.onNodeWithTag("Cancel").performClick()
        assertEquals(1,cancel)
    }
}
