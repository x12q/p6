package com.emeraldblast.p6.app.communication.event

import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.message.api.connection.service.zmq_services.msg.P6Event

/**
 * This class provides functions to find [P6Event] for an object (response, request objects, etc)
 */
interface P6EventTable{
    /**
     * find [P6Event] for an [obj]
     * @return the respective event if it exists, a fallback event if it does not
     */
    fun findEventFor(obj:Any): P6Event
    /**
     * find [P6Event] for an [obj], return a Rs
     */
    fun findEventForRs(obj:Any): Rse<P6Event>
}

