package com.qxdzbc.p6.ui.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.common.compose.TestApp
import com.qxdzbc.p6.ui.common.view.MBox

data class BState(val b:Int,val b2:Int = 0)
data class CState(val c:Int=3)
data class AState(
    val a:Int,
    val bStateMs:Ms<BState>,
    val cStateMs:Ms<CState>
)

/**
 * This example demonstrates a way to prevent unnecessary re-drawing when using nested Ms<State>.
 * The state contains of 3 obj:
 * AState, BState, CState.
 * AState holds a Ms<BState> and a Ms<CState>.
 *
 * When A change, the flat B view will be re-drawn too. To prevent this, create a dedicated composable function for B.
 *
 * To prevent A from being re-draw when B State changes: wrap the dedicated B composable in a MBox at where it is called.
 *
 * C views are separated but they are not wrap in MBox, so, when C state change, it causes the re-drawing of A.
 */
fun main() {
    TestApp(size= DpSize(200.dp,300.dp)) {
       val aStateMs = remember {
           ms(
               AState(
                   a=1,
                   bStateMs = ms(BState(2)),
                   cStateMs = ms(CState(3)),
               )
           )
       }
        var aState by aStateMs
        var bState by  aState.bStateMs
        var cState by aState.cStateMs
        Column{
            Column {
                MBox {
                    println("render A")
                    Text("A value: ${aState.a}")
                }
                // x: this is re-draw when ever A change, even when B is unchanged
                MBox{
                    println("render B")
                    Text("B value: ${bState.b2}")
                }

                MBox{
                    // x: BView2 is not re-drawn when A state change
                    // x: by wrapping this in a MBox, I can prevent A being re-drawn when B Change
                    BView2(bState)
                }

                // x: CView is not re-drawn when A's a-value is change even when cState is not remembered
                // x: this will trigger the re-draw of A too, but not B
                // x: I can prevent the re-draw of A by wrapping BOTH of these C views into MBox
                CView2(cState)
                CView3(aState.cStateMs)
            }
            Column{

            }
            Column{
                Button(onClick={
                    aState = aState.copy(a=aState.a+1)
                }){
                    Text("change A")
                }

                Button(onClick ={
                    bState = bState.copy(
                        b = bState.b + 1,
                    )
                }){
                    Text("change B")
                }

                Button(onClick ={
                    bState = bState.copy(
                        b2 = bState.b2 + 1,
                    )
                }){
                    Text("change B2")
                }

                Button(onClick={
                    cState = cState.copy(c=cState.c+1)
                }){
                    Text("change C")
                }
            }
        }
    }
}

@Composable
fun BView2(cState:BState){
    MBox{
        Row {
            MBox{
                println("render B2")
                Text("B2 value: ${cState.b2}")
            }
        }

    }
}
@Composable
fun CView2(cState:CState){
    Row {
        MBox{
            println("render C2")
            Text("C2 value: ${cState.c}")
        }
    }
}

@Composable
fun CView3(cStateMs:Ms<CState>){
    Row{
        MBox{
            println("render C3")
            Text("C3 value: ${cStateMs.value.c}")
        }
//        Button(onClick={
//            cStateMs.value = cStateMs.value.copy(
//                c=  cStateMs.value.c+1
//            )
//        }){
//            Text("change c")
//        }
    }
}

