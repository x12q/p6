package com.qxdzbc.p6.ui.example

//import py4j.GatewayServer


data class A(
    val x:Int,
    val y:String,
){
    fun increaseX():A{
        println("increaseX")
        return this.copy(x=x+1)
    }

    fun setY(newY:String):A{
        println("setY")
        return this.copy(y=newY)
    }
}

class AppZ{
    val aList:MutableList<A> = mutableListOf()
    var a:A = A(-1,"-1")
    var anything:Any? = null
        set(value) {
            field = value
            println("receive:${value}")
        }
}

interface MyAny {
    fun notify(source: Any?): Any?
}

fun main() {
    val app = AppZ()
//    val gatewayServer = GatewayServer(app)
//    println(A::class.qualifiedName)
//    gatewayServer.start()
//    println("Gateway Server Started")
}
