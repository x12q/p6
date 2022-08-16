package com.emeraldblast.p6.app.action.request_maker

import com.emeraldblast.p6.app.action.P6ResponseLegalityChecker
import com.emeraldblast.p6.message.api.connection.service.zmq_services.msg.P6Message
import com.emeraldblast.p6.message.api.connection.service.zmq_services.msg.P6Response
import com.emeraldblast.p6.ui.common.msg_api.isOk
import org.zeromq.ZMQ
import javax.inject.Inject

class BaseRMImp @Inject constructor(
    @com.emeraldblast.p6.di.EventServerSocket val eventServerSocket: ZMQ.Socket,
    private val sender: P6MessageSender,
    private val legalityChecker: P6ResponseLegalityChecker,
) : BaseRM {
    @Synchronized
    override fun send(p6Message: P6Message): P6Response {
        return sender.send(p6Message, eventServerSocket)
    }

    /**
     * send, then check for legality, check for error
     */
    @Synchronized
    override fun <T> sendThenCheck(
        p6Msg: P6Message,
        onOk: (p6Res: P6Response) -> T?,
        onErr: (p6Res: P6Response) -> Unit,
    ): T? {
        val p6Res: P6Response = send(p6Msg)
        if (p6Res.isOk()) {
            val resIsLegal = legalityChecker.check(p6Res, p6Msg.event)
            if(resIsLegal){
                return onOk(p6Res)
            }else{
                onErr(p6Res)
                return null
            }
        } else {
            onErr(p6Res)
            return null
        }
    }
}
