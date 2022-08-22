package com.qxdzbc.p6.app.code.script_builder


interface ScriptBuilder : Appendable{
    fun build(): String
    fun clear(): ScriptBuilder
    override fun append(csq: CharSequence): ScriptBuilder
    override fun append(csq: CharSequence, start: Int, end: Int): ScriptBuilder
    override fun append(c: Char): ScriptBuilder
}

