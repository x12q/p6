package com.emeraldblast.p6.app.action.request_maker.p6msg_queue_sender

import com.emeraldblast.p6.app.action.request_maker.P6MessageSender
import com.emeraldblast.p6.di.EventServerSocket
import org.zeromq.ZMQ.Socket
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject

class P6MsgRequestQueueImp @Inject constructor(
    val sender: P6MessageSender,
    @com.emeraldblast.p6.di.EventServerSocket val socket: Socket,
) : P6MsgRequestQueue {
    val queue: Queue<RequestQueueJob> = ConcurrentLinkedQueue()
    var isSending = false

    override fun queueJob(job: RequestQueueJob) {
        queue.add(job)
        if(!isSending){
            isSending = true
            while(!queue.isEmpty()){
                val topJob:RequestQueueJob? = queue.poll()
                if(topJob!=null){
                    val p6Res = sender.send(topJob.p6Msg,socket)
                    topJob.job.complete(p6Res)
                }
            }
            isSending = false
        }else{
            return
        }
    }
}
