package com.qxdzbc.p6.app.action.remote_request_maker

import com.qxdzbc.p6.app.action.remote_request_maker.p6msg_queue_sender.P6MsgRequestQueue
import com.qxdzbc.p6.app.action.remote_request_maker.p6msg_queue_sender.RequestQueueJob
import com.qxdzbc.p6.app.action.P6ResponseLegalityChecker
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Message
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response
import com.qxdzbc.p6.ui.common.msg_api.isOk
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class QueueRequestMakerImp @Inject constructor(
    private val requestQueue: P6MsgRequestQueue,
    private val legalityChecker: P6ResponseLegalityChecker,
) : QueueRequestMaker {

    override suspend fun send(p6Message: P6Message): P6Response {
        val deferredJob = CompletableDeferred<P6Response>()
        requestQueue.queueJob(
            RequestQueueJob(
                p6Msg = p6Message,
                job = deferredJob
            )
        )
        val o = deferredJob.await()
        return o
    }

    /**
     * send, then check for legality, check for error
     */
    override suspend fun <T> sendThenCheck(
        p6Msg: P6Message,
        onOk: (p6Res: P6Response) -> T?,
        onErr: (p6Res: P6Response) -> Unit,
    ): T? {
        val p6Res: P6Response = send(p6Msg)
        if (p6Res.isOk()) {
            val resIsLegal = legalityChecker.check(p6Res, p6Msg.event)
            if (resIsLegal) {
                return onOk(p6Res)
            } else {
                onErr(p6Res)
                return null
            }
        } else {
            onErr(p6Res)
            return null
        }
    }
}
