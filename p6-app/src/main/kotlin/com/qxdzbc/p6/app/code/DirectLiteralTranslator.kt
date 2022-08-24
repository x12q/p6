package com.qxdzbc.p6.app.code

import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.util.regex.Pattern

class DirectLiteralTranslator {
    companion object{
        private val strPattern = Pattern.compile("^\".*\"$", Pattern.CASE_INSENSITIVE or Pattern.DOTALL or Pattern.MULTILINE or Pattern.UNICODE_CASE or Pattern.UNICODE_CHARACTER_CLASS or Pattern.UNIX_LINES)
        fun translate(formula: String): Result<String, ErrorReport> {
            val i:Double? = formula.toDoubleOrNull()
            if(i!=null){
                return Ok(formula)
            }else{
                // wrap in triple quote, and remove the leading and trailing space
                return Ok("r\"\"\" $formula \"\"\"[1:-1]")
            }
        }
    }
}
