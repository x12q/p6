package test

import kotlin.reflect.KProperty

class GetterDelegate<T>(val f:()->T){
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return f()
    }
}

fun<T> getterDel(f:()->T)=GetterDelegate(f)
