//package com.emeraldblast.p6.ui.document.cell
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.height
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.testTag
//import androidx.compose.ui.test.*
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.unit.dp
//import com.emeraldblast.p6.app.document.cell.d.DCells
//import com.emeraldblast.p6.ui.document.cell.state.CellStateImp
//import org.junit.Test
//
//import org.junit.Rule
//
//class CellViewKtTest {
//    @get:Rule
//    val ctr = createComposeRule()
//    @Test
//    fun cellView() {
//        val text = "text"
//        val box = "box"
//        val dCell = DCells.create(1,1,"text 123","formula 123")
//        val state = CellStateImp()
//        ctr.setContent {
//            Box{
//                CellView(
//                    state = state,
//                    boxModifier = Modifier.height(100.dp).testTag(box),
//                    textModifier = Modifier.height(44.dp).testTag(text)
//                )
//            }
//        }
//        ctr.onNodeWithTag(text)
//            .assertExists()
//            .assertTextEquals(dCell.displayText)
//            .assertHeightIsEqualTo(44.dp)
//            .fetchSemanticsNode()
//
//        ctr.onNodeWithTag(box)
//            .assertHeightIsEqualTo(100.dp)
//    }
//}
