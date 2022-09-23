package com.qxdzbc.p6.app.communication.request_maker.p6msg_queue_sender

import com.qxdzbc.p6.app.action.remote_request_maker.P6MessageSender
import com.qxdzbc.p6.app.action.remote_request_maker.p6msg_queue_sender.P6MsgRequestQueueImp
import com.qxdzbc.p6.app.action.remote_request_maker.p6msg_queue_sender.RequestQueueJob
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.*
import com.google.protobuf.kotlin.toByteStringUtf8
import kotlinx.coroutines.CompletableDeferred
import org.mockito.kotlin.mock
import org.zeromq.ZMQ
import java.util.UUID
import kotlin.test.*

class P6MsgRequestQueueImpTest {
    lateinit var queue: P6MsgRequestQueueImp
    lateinit var sender: P6MessageSender

    @BeforeTest
    fun b() {
    }

    /**
     * Test that all jobs are completed, and in the order of them being queue
     */
    @Test
    fun queueJob() {
        val resultList = mutableListOf<Int>()
        sender = object : P6MessageSender {
            override fun send(p6Mesg: P6Message, socket: ZMQ.Socket): P6Response {
                Thread.sleep(100)
                resultList.add(p6Mesg.data.toStringUtf8().toInt())
                return P6Response(
                    header = P6MessageHeader(UUID.randomUUID().toString(), P6Event("e1", "e111")),
                    status = Status.OK,
                    data = "abc".toByteStringUtf8()
                )
            }
        }

        queue = P6MsgRequestQueueImp(
            sender = sender,
            socket = mock()
        )

        val jobRange = (1..30).toList()
        val jobs = jobRange.map {
            RequestQueueJob(
                p6Msg = P6Message(
                    P6MessageHeader(UUID.randomUUID().toString(), P6Event("e1", "e111")),
                    data = "${it}".toByteStringUtf8()
                ),
                job = CompletableDeferred()
            )
        }

        for (job in jobs) {
            queue.queueJob(job)
        }
        assertEquals(jobRange, resultList)
    }
}
