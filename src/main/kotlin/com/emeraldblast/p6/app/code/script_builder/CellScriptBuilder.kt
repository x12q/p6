package com.emeraldblast.p6.app.code.script_builder

interface CellScriptBuilder : ScriptBuilder {
    fun readValue(): ScriptBuilder
    fun writeValue(newValue: String): ScriptBuilder
    fun readFormula(): ScriptBuilder
    fun writeFormula(newFormula: String): ScriptBuilder
}
