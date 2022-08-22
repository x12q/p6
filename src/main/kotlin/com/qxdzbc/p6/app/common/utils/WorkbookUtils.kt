package com.qxdzbc.p6.app.common.utils

import java.util.regex.Pattern

/**
 * This object houses utility functions and objects that are related to workbooks
 */
object WorkbookUtils {
    private val defaultSheetNamePattern = Pattern.compile("Sheet(([1-9]+[0-9]*)|[0-9])")

    /**
     * Extract index number from a sheet name that conform the [defaultSheetNamePattern]. Input is assumed to be conformed with the pattern already.
     * @throws IllegalArgumentException if input name does not follow the [defaultSheetNamePattern]
     */
    private fun extractNumFromDefaultName(name: String): Int {
        var rt = ""
        for (chr in name.reversed()) {
            if (chr.isDigit()) {
                rt += chr
            } else {
                break
            }
        }
        if (rt.isEmpty()) {
            throw IllegalArgumentException("input does not comply with the pattern ${defaultSheetNamePattern.toString()}")
        }
        return rt.reversed().toInt()
    }

    /**
     * Generate a new sheet name from a given list of current names. The new name is always in the form of "Sheet<index>". Such as Sheet1, Sheet20. The indices always start at 1.The index of the new name = max index of current names + 1.
     */
    fun generateNewSheetName(currentSheetNames: List<String>): String {
        val orderedNames = currentSheetNames.filter { defaultSheetNamePattern.matcher(it).matches() }
        val indexList = orderedNames.map { extractNumFromDefaultName(it) }
        val maxIndex = if(indexList.isEmpty()){
            0
        }else{
            indexList.maxOf { it }
        }
        return "Sheet${maxIndex + 1}"
    }
}
