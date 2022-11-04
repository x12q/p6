package com.qxdzbc.p6.app.document.worksheet

import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import java.util.regex.Pattern
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
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
