package com.emeraldblast.p6.app.common.utils

class CapHashMap<K,V>(private val capacity:Int=100) : LinkedHashMap<K, V>() {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return this.size > capacity
    }
}
