package com.qxdzbc.p6.common.utils

/**
 * convert Excel-like column label to number and vice versa.
 * Eg:
 * A->1, 1-> A
 * B->2, 2-> B
 * Z->26, 26->Z
 * AA->27, 27->AA
 */
object CellLabelNumberSystem {
    private val alphabet = "abcdefghijklmnopqrstuvwxyz".uppercase().toCharArray()
    private val scale = alphabet.size

    /**
     * Convert Cell label to decimal number. Such as : "A" -> 1, "B" -> 2
     */
    fun labelToNumber(alphabetNumber: String): Int {
        var rt = 0
        for ((i, c) in alphabetNumber.uppercase().reversed().toCharArray().withIndex()) {
            val weight = Math.pow(scale.toDouble(), i.toDouble()).toInt()
            val charIndex = alphabet.indexOf(c) + 1
            val n = weight * (charIndex)
            rt += n
        }
        return rt
    }

    /**
     * convert 1-base decimal => excel col label
     * 1 -> A
     * 2 -> B
     * 27 -> AA
     * 28 -> AB
     */
    fun numberToLabel(num: Int): String {
        val result = StringBuilder()
        var n = num
        while (n > 0) {
            val index: Int = (n - 1) % 26
            result.append((index + 'A'.code).toChar())
            n = (n - 1) / 26
        }
        return result.reverse().toString()
    }
}
