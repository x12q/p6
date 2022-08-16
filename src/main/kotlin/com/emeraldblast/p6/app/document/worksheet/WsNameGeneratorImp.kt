package com.emeraldblast.p6.app.document.worksheet

import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.util.regex.Pattern
import javax.inject.Inject

class WsNameGeneratorImp @Inject constructor() : WsNameGenerator {
    companion object {
        val namePattern = Pattern.compile("Sheet[1-9][0-9]*")
    }
    override fun nextName(oldName: List<String>): Result<String, ErrorReport> {
        val nextIndex = (oldName.filter { namePattern.matcher(it).matches() }.map {
            it.substring("Sheet".length).toInt()
        }.maxOrNull() ?: 0) + 1
        val nextName = "Sheet${nextIndex}"
        return Ok(nextName)
    }
}
