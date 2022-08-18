package com.emeraldblast.p6.app.common.utils

/**
 * A hash map with a limit on its capacity. When it reaches its limit, the older elements is removed to make space for newer ones.
 */
class CapHashMap<K,V>(private val capacity:Int=100) : LinkedHashMap<K, V>() {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return this.size > capacity
    }
}
