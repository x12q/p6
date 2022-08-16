package com.emeraldblast.p6.app.document.script

import kotlin.test.Test
import kotlin.test.assertEquals

class ScriptEntryTest {

    @Test
    fun toProto_fromProto() {
        val k1 = ScriptEntryKey(
            wbKey = null,
            name = "123"
        )
        val o = ScriptEntry(
            k1, "abc"
        )
        val p = o.toProto()
        assertEquals(k1.toProto(), p.key)
        assertEquals(o.script, p.script)

        val o2 = ScriptEntry.fromProto(p)
        assertEquals(o, o2)
    }
}
