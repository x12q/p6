package com.qxdzbc.p6.ui.d_menu

import androidx.compose.material.Text
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import com.qxdzbc.p6.ui.common.view.m_menu.DownMenu
import com.qxdzbc.p6.ui.common.view.m_menu.MenuItem
import com.qxdzbc.p6.ui.common.view.m_menu.NestedRightMenu

/**
 * My own menu imp, very shitty
 */
fun main() {
    P6TestApp {
        DownMenu("Menu1"){
            MenuItem(onClick ={
                println("Do job")
            }){
                Text("do job")
            }
            NestedRightMenu("Menu 2") {
                MenuItem(onClick ={
                    println("X")
                }){
                    Text("X")
                }

                NestedRightMenu("Menu3"){
                    MenuItem(onClick ={
                        println("X2")
                    }){
                        Text("X2")
                    }
                }
            }
        }
    }
}


