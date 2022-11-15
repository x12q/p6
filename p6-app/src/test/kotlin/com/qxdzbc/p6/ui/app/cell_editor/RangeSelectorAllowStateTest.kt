package com.qxdzbc.p6.ui.app.cell_editor

import kotlin.test.*
import com.qxdzbc.p6.ui.app.cell_editor.RangeSelectorAllowState.*
internal class RangeSelectorAllowStateTest {

    @Test
    fun transit() {
        val S = START
        // x: input act char "+" at the end
        val s1 = S.transit("abc+",'+',3,null,false,false)
        assertEquals(ALLOW,s1)
        // x: input act char "/" at the end
        val s2 = s1.transit("abc+/",'/',4,null,false,false)
        assertEquals(ALLOW,s2)
        // x: input non-act char "q" at the end
        val s3 = s2.transit("abc+/q",'q',5,null,false,false)
        assertEquals(RangeSelectorAllowState.DISALLOW,s3)
    }

    @Test
    fun `test ALLOW_MOUSE`(){
        val am = ALLOW_MOUSE
        // x: input act char at the end
        assertEquals(ALLOW,am.transit("abc+",'+',3))
        // x: input act char in the middle
        assertEquals(ALLOW_MOUSE,am.transit("ab+c",'+',2))
        // x: input act char at the end
        assertEquals(DISALLOW,am.transit("abc+q",'q',4))
        // x: move cursor to act char
        assertEquals(ALLOW_MOUSE,am.transit("abc+q",null,null,4,true))
        assertEquals(ALLOW_MOUSE,am.transit("abc+q",null,null,4,false,true))

        // x: move cursor to non-act char
        assertEquals(DISALLOW,am.transit("abc+q",null,null,2,false,true))
        assertEquals(DISALLOW,am.transit("abc+q",null,null,2,true,false))
    }

    @Test
    fun `test ALLOW`(){
        val A = ALLOW

        // x: input act char at the end
        val s1 = A.transit("abc+",'+',3)
        assertEquals(ALLOW,s1)

        // x: input act char in the middle
        val s2 = A.transit("ab+c",'+',2,3)
        assertEquals(ALLOW,s2)

        // x: move cursor to next to an act char with mouse
        val s3 = A.transit("abc+/q",null,null,4,true,false)
        assertEquals(ALLOW_MOUSE, s3)

        // x: move cursor to next to a non-act char with mouse
        val s4 = A.transit("abc+",null,null,2,true,false)
        assertEquals(DISALLOW,s4)


        // x: move cursor to next to an act char with mouse
        val s5 = A.transit("abc+/q",null,null,4,false,true)
        assertEquals(ALLOW_MOUSE, s5)

        // x: move cursor to next to a non-act char with mouse
        val s6 = A.transit("abc+",null,null,2,false,true)
        assertEquals(DISALLOW,s6)

    }
}
