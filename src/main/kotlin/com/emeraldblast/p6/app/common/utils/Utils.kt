package com.emeraldblast.p6.app.common.utils

import io.grpc.stub.StreamObserver
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.ServerSocket
import java.util.*
import java.util.AbstractMap.SimpleEntry
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * This object houses common utility functions that operate on base types such as Boolean, List
 */
object Utils {

    fun <T> StreamObserver<T>.onNextAndComplete(t:T){
        this.onNext(t)
        this.onCompleted()
    }

    /**
     * Convert a boolean to "Yes"(true) or "No"(false)
     */
    fun Boolean.toYN():String{
        if(this){
            return "Yes"
        }else{
            return "No"
        }
    }

    /**
     * find and replace an item in a list
     */
    fun <T> List<T>.findAndReplace(oldItem:T, newItem:T):List<T>{
        if(oldItem == newItem){
            return this
        }else{
            val i = this.indexOf(oldItem)
            if(i>=0){
                val newl = this.toMutableList()
                newl[i]=newItem
                return newl
            }else{
                return this
            }
        }
    }

    /**
     * Find and replace a key inside a map, preserve order
     */
    fun <K,V> Map<K, V>.replaceKey(oldKey:K,newKey:K):Map<K,V>{
        val oldVal = this.get(oldKey)
        if(oldVal!=null){
            val entryList = this.entries.toMutableList()
            val oldEntry = AbstractMap.SimpleEntry(oldKey,oldVal)
            val newEntry = SimpleEntry(newKey,oldVal)
            val index = entryList.indexOf(oldEntry)
            if(index>=0){
                entryList[index] = newEntry
                return entryList.associateBy(
                    keySelector = {it.key},
                    valueTransform = {
                        it.value
                    }
                )
            }else{
                return this
            }

        }else{
            return this
        }
    }
    fun readResource(path:String):String? {
        return this.javaClass.getResource(path)?.readText()
    }

    /**
     * Find an available socket port
     */
    fun findSocketPort():Int{
        val socket = ServerSocket(0)
        val port = socket.localPort
        socket.close()
        return port
    }

    /**
     * copy a string to clipboard
     */
    fun copyTextToClipboard(text: String) {
        Toolkit.getDefaultToolkit().systemClipboard
            .setContents(StringSelection(text), null)
    }

    /**
     * compare two list ignoring order. This is very costly, use with care.
     */
    fun <T> compareListIgnoreOrder(l1:Collection<T>,l2:Collection<T>):Boolean{
        val c1 = l1.size == l2.size
        val c2 = l1.containsAll(l2) && l2.containsAll(l1)
        return c1 && c2
    }
}
