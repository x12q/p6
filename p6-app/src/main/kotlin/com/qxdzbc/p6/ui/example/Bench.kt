package com.qxdzbc.p6.ui.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.compose.TestApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main(){
    TestApp{
        val numberMs = rms(123)
        val cpNumber = rms(0)
        val cc = rememberCoroutineScope()
        Column{
            Box(
                modifier = Modifier.size(200.dp)
            ){
                Text(
                    text = "Current:"+numberMs.value.toString()
                )
            }
            Box(
                modifier = Modifier.size(200.dp)
            ){
                Text(
                    text = "Updated: ${cpNumber.value}"
                )
            }
            MButton("Read") {
                val t = Thread(){
                    println(Thread.currentThread())
                    cpNumber.value = numberMs.value
                }
                t.start()
                t.join()
            }
            MButton("Increase"){
                for (x in 0 .. 100){
                    numberMs.value++
    //                    val t = Thread(){
    //                        println(Thread.currentThread())
    //
    //                    }
    //                    t.start()
    //                    t.join()
                }
            }
        }

    }
}
