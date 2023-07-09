package com.qxdzbc.p6.app.document.workbook

import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.worksheet.WsNameGeneratorImp
import com.qxdzbc.common.compose.StateUtils.toMs
import org.junit.Test

import org.junit.Assert.*
import test.TestSample

class AutoNameWbFactoryTest {

    @Test
    fun createWb() {
        val wbl=listOf("Wb4", "Workbook3", "Book1", "book2").map {
            WorkbookImp(keyMs = WorkbookKey(it).toMs())
        }
        val testSample = TestSample()

        val wbCont: WorkbookContainer = testSample.wbCont
        wbCont.removeAll()
        wbl.forEach{wb->
            wbCont.addWb(wb)
        }
        val f = AutoNameWbFactory(wbCont = wbCont, wsNameGenerator = WsNameGeneratorImp() )
        assertEquals("Book2", f.createWbRs().component1()!!.key.name)
    }
}
