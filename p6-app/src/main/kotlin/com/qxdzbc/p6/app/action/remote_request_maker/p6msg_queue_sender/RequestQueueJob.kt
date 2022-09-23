package com.qxdzbc.p6.app.action.remote_request_maker.p6msg_queue_sender

import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Message
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response
import kotlinx.coroutines.CompletableDeferred

class RequestQueueJob(val p6Msg:P6Message,
                      val job:CompletableDeferred<P6Response>,
                )
