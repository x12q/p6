package com.qxdzbc.common

import org.apache.commons.math3.util.CombinatoricsUtils
import java.util.AbstractMap

object CollectionUtils {
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
            val newEntry = AbstractMap.SimpleEntry(newKey, oldVal)
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
    /**
     * compare two list ignoring order. This is very costly, use with care.
     */
    fun <T> compareListIgnoreOrder(l1:Collection<T>,l2:Collection<T>):Boolean{
        val c1 = l1.size == l2.size
        val c2 = l1.containsAll(l2) && l2.containsAll(l1)
        return c1 && c2
    }

    /**
     * Generate a list of combination with size = [combinationSize].
     * TODO this function is not performant enough. Lazy evaluation can speed it up.
     */
    fun <T> List<T>.generateCombinations(combinationSize:Int): List<List<T>> {
        val candidates = this
        val iterator = CombinatoricsUtils.combinationsIterator(candidates.size, combinationSize)
        val rt = mutableListOf<List<T>>()
        while(iterator.hasNext()){
            val combinationIndices = iterator.next()
            val combination = combinationIndices.map {
                candidates[it]
            }
            rt.add(combination)
        }

        return rt
    }
}
