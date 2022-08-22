package com.qxdzbc.p6.translator.jvm_translator

/**
 * For parsing a cell value
 */
interface CellLiteralParser {
    /**
     * parse a string input into either:
     * - int
     * - double
     * - TRUE/FALSE
     * - string
     * - null if input is empty string/null
     */
    fun parse(input:String?):Any?
}
