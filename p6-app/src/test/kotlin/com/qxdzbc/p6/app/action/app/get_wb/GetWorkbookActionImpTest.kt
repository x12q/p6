package com.qxdzbc.p6.app.action.app.get_wb

import com.github.michaelbull.result.Ok
import test.TestSample
import kotlin.test.*

class GetWorkbookActionImpTest{
    lateinit var ts:TestSample
    lateinit var act:GetWorkbookAction
    @BeforeTest
    fun b(){
        ts = TestSample()
        act = ts.p6Comp.getWorkbookAction()
    }
    @Test
    fun `get wb by name`(){
        val req = GetWorkbookRequest(wbName = ts.wbKey1.name)
        val rs = act.getWbRs(req)
        assertTrue(rs is Ok)
        assertEquals(ts.wbKey1,rs.value.key)
    }

    @Test
    fun `get wb by index`(){
        val req = GetWorkbookRequest(wbIndex=1)
        val rs = act.getWbRs(req)
        assertTrue(rs is Ok)
        assertEquals(ts.wbKey2,rs.value.key)
    }
}
