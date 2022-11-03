package com.qxdzbc.p6.translator.jvm_translator

import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
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
