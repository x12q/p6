package com.emeraldblast.p6.app.communication.event

import com.emeraldblast.p6.app.action.script.new_script.NewScriptRequest
import com.emeraldblast.p6.app.action.script.new_script.NewScriptResponse
import com.emeraldblast.p6.app.action.script.script_change.ScriptChangeRequest
import kotlin.reflect.KClass
import kotlin.test.*

class P6EventTableImpTest {

    lateinit var t: P6EventTableImp

    @BeforeTest
    fun b() {
        t = P6EventTableImp
    }

    fun makeMock(clazz: KClass<out Any>): WithP6EventLookupClazz {
        return object : WithP6EventLookupClazz {
            override val p6EventLookupClazz: KClass<out Any>
                get() = clazz
        }
    }

    @Test
    fun lookupScriptEvents() {
        // x: new script event
        assertEquals(P6Events.Script.NewScript.event,
            t.findEventFor(makeMock(NewScriptRequest::class)))
        assertEquals(P6Events.Script.NewScript.event,
            t.findEventFor(makeMock(NewScriptResponse::class)))

        // x: script change event
        assertEquals(P6Events.Script.ScriptChange.event,
            t.findEventFor(makeMock(ScriptChangeRequest::class)))
        assertEquals(P6Events.Script.ScriptChange.event,
            t.findEventFor(makeMock(ScriptChangeRequest::class)))
    }
}
