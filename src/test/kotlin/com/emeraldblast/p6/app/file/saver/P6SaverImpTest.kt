package com.emeraldblast.p6.app.file.saver

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.d.CellImp
import com.emeraldblast.p6.app.document.cell.d.CellValue.Companion.toCellValue
import com.emeraldblast.p6.app.document.cell.d.CellContentImp
import com.emeraldblast.p6.app.document.workbook.WorkbookImp
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.worksheet.WorksheetImp
import com.emeraldblast.p6.app.file.loader.P6FileLoaderImp
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
import com.github.michaelbull.result.Ok
import org.mockito.kotlin.mock
import test.TestSample

import java.nio.file.Paths
import kotlin.test.*

class P6SaverImpTest {
    lateinit var testSample:TestSample
    @BeforeTest
    fun b(){
        testSample = TestSample()
    }

    @Test
    fun save_load() {
        val saver = P6SaverImp()
        val loader = P6FileLoaderImp(testSample.sampleAppStateMs())
        val cell = CellImp(
            address = CellAddress("A1"),
            content = CellContentImp(
                cellValueMs = 123.toCellValue().toMs()
            )
        )
        val wb = WorkbookImp(
            keyMs = WorkbookKey("wb1").toMs(),
        ).addMultiSheetOrOverwrite(listOf(
            WorksheetImp(
                nameMs = "S1".toMs(),
                wbKeySt = mock()
            ).addOrOverwrite(cell)
        ))

        val path = Paths.get("w1.txt").toAbsolutePath()

        val srs = saver.save(wb, path)
        assertTrue { srs is Ok }
        val lRs = loader.load(path)
        assertTrue { lRs is Ok }
        val loadedWb = lRs.component1()!!

        val expectedWb = wb.setKey(WorkbookKey("w1.txt",path))
        assertEquals(expectedWb.key,loadedWb.key)
        assertEquals(expectedWb.worksheets[0],(loadedWb.worksheets[0]))
        assertEquals(expectedWb,loadedWb)
    }
}
