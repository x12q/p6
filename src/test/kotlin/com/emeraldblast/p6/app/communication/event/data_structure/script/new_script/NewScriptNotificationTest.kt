package com.emeraldblast.p6.app.communication.event.data_structure.script.new_script

import com.emeraldblast.p6.app.action.script.new_script.NewScriptNotification
import com.emeraldblast.p6.app.action.common_data_structure.ErrorIndicator
import org.mockito.kotlin.mock
import test.TestSample
import kotlin.test.*

class NewScriptNotificationTest {

    @Test
    fun isLegal() {
        assertTrue { NewScriptNotification(emptyList(), ErrorIndicator.noError).isLegal() }
        assertTrue { NewScriptNotification(listOf(mock(),mock()), ErrorIndicator.noError).isLegal() }
        assertFalse { NewScriptNotification(listOf(mock(),mock()), ErrorIndicator.error(TestSample.sampleErrorReport)).isLegal() }
        assertTrue { NewScriptNotification(listOf(), ErrorIndicator.error(TestSample.sampleErrorReport)).isLegal() }
    }
}
