package com.emeraldblast.p6.app.action.request_maker.p6msg_queue_sender

import com.emeraldblast.p6.message.api.connection.service.zmq_services.msg.P6Message
import com.emeraldblast.p6.message.api.connection.service.zmq_services.msg.P6Response
import kotlinx.coroutines.CompletableDeferred

class RequestQueueJob(val p6Msg:P6Message,
                      val job:CompletableDeferred<P6Response>,
                )
