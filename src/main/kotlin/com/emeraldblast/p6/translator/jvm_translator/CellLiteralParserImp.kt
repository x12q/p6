package com.emeraldblast.p6.translator.jvm_translator

import javax.inject.Inject

class CellLiteralParserImp @Inject constructor() : CellLiteralParser {
    override fun parse(input: String?): Any? {
        if(input == null){
            return null
        }
        if(input.isEmpty()){
            return null
        }
        val integer = input.toIntOrNull()
        if(integer!=null){
            return integer
        }
        val double = input.toDoubleOrNull()
        if(double!=null){
            return double
        }

        if(input == "TRUE"){
            return true
        }
        if(input == "FALSE"){
            return false
        }
        return input
    }
}
