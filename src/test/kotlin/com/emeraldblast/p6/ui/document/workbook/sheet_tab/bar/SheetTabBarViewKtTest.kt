package com.emeraldblast.p6.ui.document.workbook.sheet_tab.bar

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class SheetTabBarViewKtTest {
    @get:Rule
    val cr = createComposeRule()

    @Test
    fun sheetTabBarView() {
//        var a = 0
//        var b = 0
//        var oState: SheetTabBarState? = null
//        cr.setContent {
//            Box {
//                val activeSheetPointer:Ms<ActiveWorksheetPointer> = ms(ActiveWorksheetPointerImp(null))
//                val state: MutableState<SheetTabBarState> = remember {
//                    mutableStateOf(SheetTabBarStateImp(
//                        activeSheetPointerMs = activeSheetPointer,
//                    ))
//                }
//                oState = state.value
//                Box(modifier = Modifier.size(500.dp, 70.dp).then(R.border.mod.black)) {
//                    Column {
//                        SheetTabBarView(
//                            state=state.value,
//                            onItemClick = {
//                                val newState = state.value.select(it)
//                                state.value = newState
//                                oState = newState
//                                a += 1
//                            }, onNewButtonClick = { b += 1 })
//                    }
//                }
//            }
//        }
//        cr.onNodeWithText("sheet1").performClick()
//        assertEquals(1, a)
//        assertEquals("sheet1", oState!!.activeSheetPointer.worksheetName)
//        cr.onNodeWithText("sheet2").performClick()
//        assertEquals("sheet2", oState!!.activeSheetPointer.worksheetName)
//        assertEquals(2, a)
//        cr.onNodeWithText(R.text.plus).performClick()
//        assertEquals(1, b)
    }
}
