package com.emeraldblast.p6.app.document.workbook

import com.emeraldblast.p6.app.document.wb_container.WorkbookContainer
import com.emeraldblast.p6.app.document.worksheet.WsNameGeneratorImp
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
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

        val contMs: Ms<WorkbookContainer> = testSample.wbContMs
        contMs.value = contMs.value.removeAll()
        contMs.value = wbl.fold(contMs.value){acc,wb->
            acc.addWb(wb)
        }
        val f = AutoNameWbFactory(wbContMs = contMs, wsNameGenerator = WsNameGeneratorImp() )
        assertEquals("Book2", f.createWbRs().component1()!!.key.name)
    }
}
