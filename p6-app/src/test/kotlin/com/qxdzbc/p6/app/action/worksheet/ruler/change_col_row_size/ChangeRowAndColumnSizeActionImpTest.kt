package com.qxdzbc.p6.app.action.worksheet.ruler.change_col_row_size

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import test.BaseAppStateTest
import java.util.*
import kotlin.test.*

internal class ChangeRowAndColumnSizeActionImpTest : BaseAppStateTest(){

    lateinit var action:ChangeRowAndColumnSizeActionImp
    lateinit var targetWbWsSt:WbWsSt
    @BeforeTest
    fun b(){
        action = ts.comp.changeRowAndColSizeActionImp()
        targetWbWsSt=ts.wb1Ws1St
    }

    @Test
    fun makeCommandToChangeRowHeight() {
        val rowIndex = 1
        val dif = 100.dp

        val command = action.makeCommandToChangeRowHeight(rowIndex,dif,targetWbWsSt)
        fun getHeight():Dp?{
            return ts.sc.getWsState(targetWbWsSt)!!.getRowHeight(rowIndex)
        }
        fun `change workbook key between command invocations`(){
            sc.getWbKeyMs(targetWbWsSt.wbKey)?.also {
                it.value = it.value.setName(UUID.randomUUID().toString())
            }
        }
        preCondition {
            getHeight().shouldBeNull()
        }

        command.run()
        postCondition {
            getHeight() shouldBe ts.sc.getWsState(targetWbWsSt)?.defaultRowHeight!! + dif
        }

        `change workbook key between command invocations`()
        command.undo()
        postCondition {
            getHeight().shouldBeNull()
        }

        `change workbook key between command invocations`()
        command.run()
        postCondition {
            getHeight() shouldBe ts.sc.getWsState(targetWbWsSt)?.defaultRowHeight!! + dif
        }
    }

    @Test
    fun makeCommandToChageColWidth() {
        val colIndex = 1
        val dif = 100.dp

        val command = action.makeCommandToChageColWidth(colIndex,dif,targetWbWsSt)
        fun getWidth():Dp?{
            return ts.sc.getWsState(targetWbWsSt)!!.getColumnWidth(colIndex)
        }
        fun `change workbook key between command invocations`(){
            sc.getWbKeyMs(targetWbWsSt.wbKey)?.also {
                it.value = it.value.setName(UUID.randomUUID().toString())
            }
        }
        preCondition {
            getWidth().shouldBeNull()
        }

        command.run()
        postCondition {
            getWidth() shouldBe ts.sc.getWsState(targetWbWsSt)?.defaultColWidth!! + dif
        }

        `change workbook key between command invocations`()
        command.undo()
        postCondition {
            getWidth().shouldBeNull()
        }

        `change workbook key between command invocations`()
        command.run()
        postCondition {
            getWidth() shouldBe ts.sc.getWsState(targetWbWsSt)?.defaultColWidth!! + dif
        }
    }
}
