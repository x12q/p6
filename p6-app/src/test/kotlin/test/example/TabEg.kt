package test.example

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.common.view.BorderBox

/**
 * See console for detail
 */
@OptIn(ExperimentalFoundationApi::class)
fun main() {
    singleWindowApplication {
        var selectedIndexMs = remember { mutableStateOf(0) }
        var selectedIndex: Int by selectedIndexMs
        val list = (1..20).toList().map { it.toString() }
        ScrollableTabRow(
            edgePadding = 0.dp,
            selectedTabIndex = selectedIndex,
            backgroundColor = Color.Blue,
            modifier = Modifier
                .height(50.dp),
            indicator = { tabPositions: List<TabPosition> ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedIndex])
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(Color.Red)
                )
            }
        ) {
            list.forEachIndexed { index, text ->
                val selected = selectedIndex == index
                Tab(
                    modifier = Modifier
                        .background(
                            Color.White
                        ).requiredWidthIn(P6R.size.value.minTabWidth, P6R.size.value.maxTabWidth),
                    selected = selected,
                    onClick = { selectedIndex = index },
                ) {
                    BorderBox(modifier = Modifier.fillMaxSize()) {
                        BasicText(text)
                    }
                }
            }
        }
    }
}
