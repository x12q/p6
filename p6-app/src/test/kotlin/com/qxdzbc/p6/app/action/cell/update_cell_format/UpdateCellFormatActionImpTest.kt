package com.qxdzbc.p6.app.action.cell.update_cell_format

import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.flyweight.FlyweightTableImp
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.cell.state.CellStates
import com.qxdzbc.p6.ui.document.cell.state.format.cell.CellFormatImp
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormatImp
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format.CellFormatFlyweightTable
import com.qxdzbc.p6.ui.format.CellFormatFlyweightTableImp
import com.qxdzbc.p6.ui.format.attr.BoolAttr
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import test.BaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class UpdateCellFormatActionImpTest : BaseTest() {

    lateinit var action: UpdateCellFormatActionImp
    lateinit var cellFormatTableMs: Ms<CellFormatFlyweightTable>
    val ffTable get() = cellFormatTableMs.value.floatTable
    lateinit var wbwsSt: WbWsSt
    val cellA1Id get() = CellId(CellAddress("A1"), wbwsSt)
    val cellB1Id get() = CellId(CellAddress("B1"), wbwsSt)
    val cellStateA1 = CellStates.blank(CellAddress("A1"))
    val cellStateB1 = CellStates.blank(CellAddress("B1"))


    @BeforeTest
    override fun _b() {
        super._b()
        cellFormatTableMs = ms(CellFormatFlyweightTableImp())
        action = UpdateCellFormatActionImp(
            cellFormatTableMs = cellFormatTableMs,
            stateContainerSt = comp.stateContMs()
        )
        wbwsSt = ts.sc.getWbWsSt(ts.wbKey1, ts.wsn1)!!
    }

}
