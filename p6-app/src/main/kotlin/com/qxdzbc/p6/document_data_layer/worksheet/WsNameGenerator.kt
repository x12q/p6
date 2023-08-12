package com.qxdzbc.p6.document_data_layer.worksheet

import com.qxdzbc.common.error.SingleErrorReport
import com.github.michaelbull.result.Result
interface WsNameGenerator {
    fun nextName(oldName:List<String> = emptyList()):Result<String,SingleErrorReport>
}
