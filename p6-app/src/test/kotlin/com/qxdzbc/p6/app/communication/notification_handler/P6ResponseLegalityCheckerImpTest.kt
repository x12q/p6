package com.qxdzbc.p6.app.communication.notification_handler

import com.qxdzbc.p6.app.action.P6ResponseLegalityCheckerImp
import com.qxdzbc.p6.app.communication.event.P6Events
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Messages
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.Status
import com.qxdzbc.p6.ui.app.error_router.ErrorRouterImp
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.google.protobuf.ByteString
import test.TestSample

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class P6ResponseLegalityCheckerImpTest {

    lateinit var handler: P6ResponseLegalityCheckerImp
    lateinit var appStateMs: Ms<AppState>
    val appState get() = appStateMs.value
    lateinit var testSample: TestSample
    @BeforeTest
    fun b() {
        testSample = TestSample()
        appStateMs = testSample.sampleAppStateMs()
        handler = P6ResponseLegalityCheckerImp(ErrorRouterImp(appStateMs))
    }

    @Test
    fun `checkEvent ok case`() {
        val input = P6Messages.p6Response(
            event = P6Events.Worksheet.Rename.event,
            data = ByteString.EMPTY,
            status = Status.OK
        )
        assertTrue { appState.oddityContainer.isEmpty() }
        assertTrue { handler.checkEvent(input, input.header.eventType) }
        assertTrue { appState.oddityContainer.isEmpty() }
    }

    @Test
    fun `checkEvent error case`() {
        val input = P6Messages.p6Response(
            event = P6Events.Worksheet.Rename.event,
            data = ByteString.EMPTY,
            status = Status.OK
        )
        assertTrue(appState.oddityContainer.isEmpty())
        assertFalse { handler.checkEvent(input, P6Events.unknown) }
        assertTrue { appState.oddityContainer.isNotEmpty() }
    }

    @Test
    fun `stdCheck error case`(){
        val input = P6Messages.p6Response(
            event = P6Events.Worksheet.Rename.event,
            data = ByteString.EMPTY,
            status = Status.OK
        )
        assertTrue(appState.oddityContainer.isEmpty())
        assertFalse(handler.check(input,P6Events.unknown))
        assertTrue(appState.oddityContainer.isNotEmpty())
    }

    @Test
    fun `stdCheck ok case`(){
        val input = P6Messages.p6Response(
            event = P6Events.Worksheet.Rename.event,
            data = ByteString.EMPTY,
            status = Status.OK
        )
        assertTrue(handler.check(input,P6Events.Worksheet.Rename.event))
    }
}
