package com.qxdzbc.p6.app.action.remote_request_maker.p6msg_queue_sender

/**
 * A queue for request to back end, this ensure that:
 * - requests are processed in the order of the queue, and
 * - no request is dropped
 * - no request encounters sockets in illegal state
 * - only one request is processed at a time
 */
interface P6MsgRequestQueue {
    fun queueJob(job: RequestQueueJob)
}
