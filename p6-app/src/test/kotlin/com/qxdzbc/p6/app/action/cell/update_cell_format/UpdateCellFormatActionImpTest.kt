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
    override fun b() {
        super.b()
        cellFormatTableMs = ms(CellFormatFlyweightTableImp())
        action = UpdateCellFormatActionImp(
            cellFormatTableMs = cellFormatTableMs,
            stateContainerSt = comp.stateContMs()
        )
        wbwsSt = ts.sc.getWbWsSt(ts.wbKey1, ts.wsn1)!!
    }

    @Test
    fun produceNewStateForNewBackgroundColor(){
        val c1 = Color(123)
        val c2 = Color(456)
        test("set cell A1 background color: $c1 -> $c2") {
            val cellFormatTable = CellFormatFlyweightTableImp().updateColorTable(
                FlyweightTableImp<Color>()
                    .addOrUpdate(c1)
                    .addOrUpdate(c1)
            )
            val inputState = UpdateCellFormatActionImp.TargetState(
                cellState = cellStateA1.setCellFormat(
                    CellFormatImp(backgroundColor = c1)
                ),
                cellFormatTable = cellFormatTable
            )
            val newState = action.produceNewStateForNewBackgroundColor(
                inputState = inputState,
                color = c2
            )
            postCondition {
                val expectedState = UpdateCellFormatActionImp.TargetState(
                    inputState.cellState.setCellFormat(
                        CellFormatImp(backgroundColor = c2)
                    ),
                    inputState.cellFormatTable.updateColorTable(
                        inputState.cellFormatTable.colorTable
                            .reduceCount(c1)
                            .increaseCount(c2)
                    )
                )
                newState shouldBe expectedState
            }
        }
    }

    @Test
    fun produceNewStateForNewVerticalAlignment() {
        test("set cell A1 vertical alignment: Top -> Bot") {
            val cellFormatTable = CellFormatFlyweightTableImp().updateVerticalAlignmentTable(
                FlyweightTableImp<TextVerticalAlignment>()
                    .addOrUpdate(TextVerticalAlignment.Center)
                    .addOrUpdate(TextVerticalAlignment.Center)
                    .addOrUpdate(TextVerticalAlignment.Top)
                    .addOrUpdate(TextVerticalAlignment.Top)
            )
            val inputState = UpdateCellFormatActionImp.TargetState(
                cellState = cellStateA1.setVerticalAlignment(TextVerticalAlignment.Top),
                cellFormatTable = cellFormatTable
            )
            val newState = action.produceNewStateForNewVerticalAlignment(
                inputState = inputState,
                alignment = TextVerticalAlignment.Bot
            )
            postCondition {
                val expectedState = UpdateCellFormatActionImp.TargetState(
                    inputState.cellState.setVerticalAlignment(TextVerticalAlignment.Bot),
                    inputState.cellFormatTable.updateVerticalAlignmentTable(
                        inputState.cellFormatTable.verticalAlignmentTable
                            .reduceCount(TextVerticalAlignment.Top)
                            .increaseCount(TextVerticalAlignment.Bot)
                    )
                )
                newState shouldBe expectedState
            }
        }
    }

    @Test
    fun produceNewStateForNewUnderlined() {
        test("set cell A1 underlined: true -> false") {
            val cellFormatTable = CellFormatFlyweightTableImp().updateBoolTable(
                FlyweightTableImp<BoolAttr>()
                    .addOrUpdate(BoolAttr.TRUE)
                    .addOrUpdate(BoolAttr.TRUE)
                    .addOrUpdate(BoolAttr.FALSE)
                    .addOrUpdate(BoolAttr.FALSE)
            )
            val inputState = UpdateCellFormatActionImp.TargetState(
                cellState = cellStateA1.setTextUnderlined(true),
                cellFormatTable = cellFormatTable
            )
            val newState = action.produceNewStateForNewUnderlined(
                inputState = inputState,
                underlined = false
            )
            postCondition {
                val expectedState = UpdateCellFormatActionImp.TargetState(
                    inputState.cellState.setTextUnderlined(false),
                    inputState.cellFormatTable.updateBoolTable(
                        inputState.cellFormatTable.boolTable
                            .reduceCount(BoolAttr.TRUE)
                            .increaseCount(BoolAttr.FALSE)
                    )
                )
                newState shouldBe expectedState
            }
        }
    }

    @Test
    fun produceNewStateForNewCrossed() {
        test("set cell A1 crossed: true -> false") {
            val cellFormatTable = CellFormatFlyweightTableImp().updateBoolTable(
                FlyweightTableImp<BoolAttr>()
                    .addOrUpdate(BoolAttr.TRUE)
                    .addOrUpdate(BoolAttr.TRUE)
                    .addOrUpdate(BoolAttr.FALSE)
                    .addOrUpdate(BoolAttr.FALSE)
            )
            val inputState = UpdateCellFormatActionImp.TargetState(
                cellState = cellStateA1.setTextCrossed(true),
                cellFormatTable = cellFormatTable
            )
            val newState = action.produceNewStateForNewCrossed(
                inputState = inputState,
                crossed = false
            )
            postCondition {
                val expectedState = UpdateCellFormatActionImp.TargetState(
                    inputState.cellState.setTextCrossed(false),
                    inputState.cellFormatTable.updateBoolTable(
                        inputState.cellFormatTable.boolTable
                            .reduceCount(BoolAttr.TRUE)
                            .increaseCount(BoolAttr.FALSE)
                    )
                )
                newState shouldBe expectedState
            }

        }
    }

    @Test
    fun produceNewStateForNewHorizontalAlignment() {
        test("set cell A1 text horizontal alignment: Center -> End") {
            val cellFormatTable = CellFormatFlyweightTableImp().updateHorizontalAlignmentTable(
                FlyweightTableImp<TextHorizontalAlignment>()
                    .addOrUpdate(TextHorizontalAlignment.Center)
                    .addOrUpdate(TextHorizontalAlignment.Center)
                    .addOrUpdate(TextHorizontalAlignment.Start)
                    .addOrUpdate(TextHorizontalAlignment.Start)
            )
            val inputState = UpdateCellFormatActionImp.TargetState(
                cellState = cellStateA1.setHorizontalAlignment(TextHorizontalAlignment.Center),
                cellFormatTable = cellFormatTable
            )
            val newState = action.produceNewStateForNewHorizontalAlignment(
                inputState = inputState,
                alignment = TextHorizontalAlignment.End
            )
            postCondition {
                val expectedState = UpdateCellFormatActionImp.TargetState(
                    inputState.cellState.setHorizontalAlignment(TextHorizontalAlignment.End),
                    inputState.cellFormatTable.updateHorizontalAlignmentTable(
                        inputState.cellFormatTable.horizontalAlignmentTable
                            .reduceCount(TextHorizontalAlignment.Center)
                            .addOrUpdate(TextHorizontalAlignment.End)

                    )
                )
                newState shouldBe expectedState
            }
        }
    }

    @Test
    fun applyNewState() {
        val newState = UpdateCellFormatActionImp.TargetState(
            cellState = CellStates.blank(cellA1Id.address),
            cellFormatTable = CellFormatFlyweightTableImp().updateFloatTable(
                FlyweightTableImp<Float>().addOrUpdate(123f)
            )
        )
        test("update app state new state") {
            preCondition {
                sc.getCellState(cellA1Id) shouldNotBe newState.cellState
                cellFormatTableMs.value shouldNotBe newState.cellFormatTable
            }
            action.applyNewState(cellA1Id, newState)
            postCondition {
                sc.getCellState(cellA1Id) shouldBe newState.cellState
                cellFormatTableMs.value shouldBe newState.cellFormatTable
            }
        }
    }

    @Test
    fun produceNewState() {
        val cs = cellStateA1
        test("old format ref count is reduced, and new format attr is recorded") {
            val cellFormatTable = CellFormatFlyweightTableImp().updateFloatTable(
                FlyweightTableImp<Float>()
                    .addOrUpdate(1f)
                    .addOrUpdate(1f)
                    .addOrUpdate(2f)
                    .addOrUpdate(2f)
            )
            val newTextFormat = TextFormatImp().copy(textSize = 3f)
            val newState = action.produceNewStateWithNewTextFormat(
                cs,
                newFormat = 3f,
                getCurrentFormat = {
                    1f
                },
                getFlyweightTable = {
                    cellFormatTable.floatTable
                },
                produceNewTextFormat = { t, tf ->
                    newTextFormat
                },
                produceNewCellFormatTable = {
                    cellFormatTable.updateFloatTable(it)
                }
            )
            postCondition {
                val expectedCellFormatTable = CellFormatFlyweightTableImp().updateFloatTable(
                    FlyweightTableImp<Float>()
                        .addOrUpdate(1f)
                        .addOrUpdate(2f)
                        .addOrUpdate(2f)
                        .addOrUpdate(3f)
                )
                newState.shouldNotBeNull()
                newState.cellState.textFormat shouldBe newTextFormat
                newState.cellFormatTable shouldBe expectedCellFormatTable
            }
        }

        test("old format ref count is reduced to 0 and removed, and new format attr is recorded") {
            val cellFormatTable = CellFormatFlyweightTableImp().updateFloatTable(
                FlyweightTableImp<Float>()
                    .addOrUpdate(1f)
                    .addOrUpdate(2f)
                    .addOrUpdate(2f)
            )
            val newTextFormat = TextFormatImp().copy(textSize = 3f)
            val newState = action.produceNewStateWithNewTextFormat(
                cs,
                newFormat = 3f,
                getCurrentFormat = {
                    1f
                },
                getFlyweightTable = {
                    cellFormatTable.floatTable
                },
                produceNewTextFormat = { t, tf ->
                    newTextFormat
                },
                produceNewCellFormatTable = {
                    cellFormatTable.updateFloatTable(it)
                }
            )
            postCondition {
                val expectedCellFormatTable = CellFormatFlyweightTableImp().updateFloatTable(
                    FlyweightTableImp<Float>()
                        .addOrUpdate(2f)
                        .addOrUpdate(2f)
                        .addOrUpdate(3f)
                )
                newState.shouldNotBeNull()
                newState.cellState.textFormat shouldBe newTextFormat
                newState.cellFormatTable shouldBe expectedCellFormatTable
            }
        }
    }


    @Test
    fun produceNewStateForNewTextColor() {
        val c1 = Color(123)
        val c2 = Color(456)
        test("set cell A1 text color: $c1 -> $c2") {
            val cellFormatTable = CellFormatFlyweightTableImp().updateColorTable(
                FlyweightTableImp<Color>()
                    .addOrUpdate(c1)
                    .addOrUpdate(c2)
            )
            val inputState = UpdateCellFormatActionImp.TargetState(
                cellState = cellStateA1.setTextFormat(TextFormat.createDefaultTextFormat().setTextColor(c1)),
                cellFormatTable = cellFormatTable
            )
            val newState = action.produceNewStateForNewTextColor(
                inputState = inputState,
                color = c2
            )
            postCondition {
                val expectedState = UpdateCellFormatActionImp.TargetState(
                    inputState.cellState.setTextFormat(TextFormat.createDefaultTextFormat().setTextColor(c2)),
                    inputState.cellFormatTable.updateColorTable(
                        inputState.cellFormatTable.colorTable
                            .reduceCount(c1).addOrUpdate(c2)
                    )
                )
                newState shouldBe expectedState
            }
        }

    }

    @Test
    fun produceNewStateForNewTextSize() {
        val v1 = 123f
        val v2 = 456f
        val v3 = 333f

        test("set cell A1 text size: 123 -> 456") {
            val cellFormatTable = CellFormatFlyweightTableImp().updateFloatTable(
                FlyweightTableImp<Float>()
                    .addOrUpdate(v1)
                    .addOrUpdate(v1)
            )
            val inputState = UpdateCellFormatActionImp.TargetState(
                cellState = cellStateA1.setTextFormat(TextFormat.createDefaultTextFormat().setTextSize(v1)),
                cellFormatTable = cellFormatTable
            )
            val newState = action.produceNewStateForNewTextSize(
                inputState = inputState,
                textSize = v2
            )
            postCondition {
                val expectedState = UpdateCellFormatActionImp.TargetState(
                    inputState.cellState.setTextFormat(TextFormat.createDefaultTextFormat().setTextSize(v2)),
                    inputState.cellFormatTable.updateFloatTable(
                        inputState.cellFormatTable.floatTable
                            .reduceCount(v1).addOrUpdate(v2)
                    )
                )
                newState shouldBe expectedState
            }
        }


    }
}