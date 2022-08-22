package com.qxdzbc.p6.app.communication.event

import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Event
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlin.reflect.KClass

object P6EventTableImp : P6EventTable {
    private fun List<P6EventMetaDef>.toMap(): Map<Any, P6Event> {
        val map = mutableMapOf<Any, P6Event>()
        for (defPack in this) {
            defPack.Request?.also { req ->
                map[req] = defPack.event
            }
            map[defPack.Response] = defPack.event
            for (o in defPack.others) {
                map[o] = defPack.event
            }
        }
        return map
    }

    private val map: Map<Any, P6Event> = P6Events.allP6EventDefs.toMap()

    override fun findEventFor(obj: Any): P6Event {

        val e = this.__findEventFor(obj) ?: P6Events.unknown
        return e
    }

    private fun __findEventFor(res: Any): P6Event? {
        val clazz: KClass<out Any> = if (res is WithP6EventLookupClazz) {
            res.p6EventLookupClazz
        } else {
            res::class
        }
        val e = map[clazz]
        return e
    }

//    fun findEventFor(obj: Any, ifHaveEvent: (P6Event) -> Unit) {
//        val e = this.__findEventFor(obj)
//        if (e != null) {
//            ifHaveEvent(e)
//        }
//    }
//
//    fun findEventForRs(obj: Any, onResult: (Result<P6Event, ErrorReport>) -> Unit) {
//        onResult(this.findEventForRs(obj))
//    }

    override fun findEventForRs(obj: Any): Result<P6Event, ErrorReport> {
        val e = this.__findEventFor(obj)
        if (e != null) {
            return Ok(e)
        } else {
            return Err(P6EventErrors.NoEventForObj(obj))
        }
    }
}


