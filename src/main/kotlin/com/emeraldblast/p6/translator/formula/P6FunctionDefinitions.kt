package com.emeraldblast.p6.translator.formula

/**
 * Contain the default function/formula definitions
 */
interface P6FunctionDefinitions {

    companion object {
        /**
         * name of internal function for getting sheets
         */
        val getSheetRs = "__getSheetRs"
        /**
         * name of internal function for getting ranges
         */
        val getRangeRs = "__getRangeRs"
        /**
         * name of internal function for getting cells
         */
        val getCellRs = "__getCellRs"
    }
    val functionMap: Map<String, FunctionDef>
}
