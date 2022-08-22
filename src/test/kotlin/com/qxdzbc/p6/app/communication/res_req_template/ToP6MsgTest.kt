package com.qxdzbc.p6.app.communication.res_req_template

import com.qxdzbc.p6.app.communication.event.P6Events
import com.qxdzbc.p6.app.communication.event.WithP6EventLookupClazz
import com.qxdzbc.p6.app.action.script.new_script.NewScriptResponse
import com.google.protobuf.ByteString
import com.google.protobuf.kotlin.toByteStringUtf8
import kotlin.reflect.KClass
import kotlin.test.*

class ToP6MsgTest{
    @Test
    fun toP6Msg1(){
        val data = "abc".toByteStringUtf8()
        val o=object:ToP6Msg {
            override fun toProtoBytes(): ByteString {
                return data
            }
        }

        val p6Msg = o.toP6Msg()
        assertEquals(P6Events.unknown,p6Msg.event)
        assertEquals(data,p6Msg.data)
    }

    @Test
    fun toP6Msg2(){
        val data = "abc".toByteStringUtf8()
        val o=object:ToP6Msg,WithP6EventLookupClazz {
            override fun toProtoBytes(): ByteString {
                return data
            }

            override val p6EventLookupClazz: KClass<out Any>
                get() = NewScriptResponse::class
        }

        val p6Msg = o.toP6Msg()
        assertEquals(P6Events.Script.NewScript.event,p6Msg.event)
        assertEquals(data,p6Msg.data)
    }
}
