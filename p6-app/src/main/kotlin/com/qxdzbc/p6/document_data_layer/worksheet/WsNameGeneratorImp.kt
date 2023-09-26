package com.qxdzbc.p6.document_data_layer.worksheet

import com.qxdzbc.common.error.SingleErrorReport
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.p6.di.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class WsNameGeneratorImp @Inject constructor() : WsNameGenerator {
    companion object {
        val namePattern = Pattern.compile("Sheet[1-9][0-9]*")
    }
    override fun nextName(oldName: List<String>): Result<String, SingleErrorReport> {
        val nextIndex = (oldName.filter { namePattern.matcher(it).matches() }.map {
            it.substring("Sheet".length).toInt()
        }.maxOrNull() ?: 0) + 1
        val nextName = "Sheet${nextIndex}"
        return Ok(nextName)
    }
}
