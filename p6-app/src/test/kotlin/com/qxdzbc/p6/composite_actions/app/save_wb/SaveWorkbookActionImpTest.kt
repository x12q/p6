package com.qxdzbc.p6.composite_actions.app.save_wb

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import test.TestSample
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.test.*

class SaveWorkbookActionImpTest {
    lateinit var ts:TestSample
    lateinit var act:SaveWorkbookAction
    @BeforeTest
    fun b(){
        ts = TestSample()
        act = ts.comp.saveWbAction()
    }

    @Test
    fun saveWorkbook() {
        val path = Paths.get("twb.txt")

        act.saveWorkbook(ts.wbKey1,path)

        ts.wbKey1.name shouldBe path.fileName.toString()
        ts.wbKey1.path?.toAbsolutePath() shouldBe path.toAbsolutePath()

        val wb = ts.sc.getWb(ts.wbKey1)
        wb shouldNotBe null
        if(path.exists()){
            Files.delete(path)
        }
    }
}
