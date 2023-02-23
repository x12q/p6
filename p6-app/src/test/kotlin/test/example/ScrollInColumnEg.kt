package test.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.compose.P6TestApp

@OptIn(ExperimentalMaterialApi::class)
fun main(){
    P6TestApp {
        AlertDialog(
            modifier =Modifier.height(300.dp).width(300.dp),
            onDismissRequest = {},
            text =  {
                val ss = rememberScrollState(0)
                Text("import androidx.compose.foundation.background\n" +
                        "import androidx.compose.foundation.layout.Box\n" +
                        "import androidx.compose.foundation.layout.Spacer\n" +
                        "import androidx.compose.foundation.layout.fillMaxHeight\n" +
                        "import androidx.compose.foundation.layout.fillMaxSize\n" +
                        "import androidx.compose.foundation.layout.fillMaxWidth\n" +
                        "import androidx.compose.foundation.layout.height\n" +
                        "import androidx.compose.foundation.layout.padding\n" +
                        "import androidx.compose.foundation.lazy.LazyColumn\n" +
                        "import androidx.compose.foundation.lazy.rememberLazyListState\n" +
                        "import androidx.compose.foundation.rememberScrollbarAdapter\n" +
                        "import androidx.compose.material.Text\n" +
                        "import androidx.compose.runtime.Composable\n" +
                        "import androidx.compose.ui.Alignment\n" +
                        "import androidx.compose.ui.Modifier\n" +
                        "import androidx.compose.ui.graphics.Color\n" +
                        "import androidx.compose.ui.unit.dp\n" +
                        "import androidx.compose.ui.window.Window\n" +
                        "import androidx.compose.ui.window.application\n" +
                        "import androidx.compose.ui.window.rememberWindowState"+
                        "import androidx.compose.foundation.layout.height\n" +
                        "import androidx.compose.foundation.layout.padding\n" +
                        "import androidx.compose.foundation.lazy.LazyColumn\n" +
                        "import androidx.compose.foundation.lazy.rememberLazyListState\n" +
                        "import androidx.compose.foundation.rememberScrollbarAdapter\n" +
                        "import androidx.compose.material.Text\n" +
                        "import androidx.compose.runtime.Composable\n" +
                        "import androidx.compose.ui.Alignment\n" +
                        "import androidx.compose.ui.Modifier\n" +
                        "import androidx.compose.ui.graphics.Color\n" +
                        "import androidx.compose.ui.unit.dp\n" +
                        "import androidx.compose.ui.window.Window\n" +
                        "import androidx.compose.ui.window.application\n" +
                        "import androidx.compose.ui.window.rememberWindowState"
                    ,
                modifier = Modifier.verticalScroll(ss)
                )
            }            ,
            buttons = {
                Box{}
            }
        )
//        Column{
//            for(x in 0 .. 10){
//                BorderBox {
//                    Text("+=====${x}=====+")
//                }
//            }
//            val ss = rememberScrollState(0)
//            BorderBox (modifier= Modifier.height(50.dp).width(100.dp).verticalScroll(ss)) {
//                Text("import androidx.compose.foundation.VerticalScrollbar\n" +
//                        "import androidx.compose.foundation.background\n" +
//                        "import androidx.compose.foundation.layout.Box\n" +
//                        "import androidx.compose.foundation.layout.Spacer\n" +
//                        "import androidx.compose.foundation.layout.fillMaxHeight\n" +
//                        "import androidx.compose.foundation.layout.fillMaxSize\n" +
//                        "import androidx.compose.foundation.layout.fillMaxWidth\n" +
//                        "import androidx.compose.foundation.layout.height\n" +
//                        "import androidx.compose.foundation.layout.padding\n" +
//                        "import androidx.compose.foundation.lazy.LazyColumn\n" +
//                        "import androidx.compose.foundation.lazy.rememberLazyListState\n" +
//                        "import androidx.compose.foundation.rememberScrollbarAdapter\n" +
//                        "import androidx.compose.material.Text\n" +
//                        "import androidx.compose.runtime.Composable\n" +
//                        "import androidx.compose.ui.Alignment\n" +
//                        "import androidx.compose.ui.Modifier\n" +
//                        "import androidx.compose.ui.graphics.Color\n" +
//                        "import androidx.compose.ui.unit.dp\n" +
//                        "import androidx.compose.ui.window.Window\n" +
//                        "import androidx.compose.ui.window.application\n" +
//                        "import androidx.compose.ui.window.rememberWindowState")
//            }
//
//            for(x in 0 .. 1000){
//                BorderBox {
//                    Text("+=====${x}=====+")
//                }
//            }
//        }

    }
}
