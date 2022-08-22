package com.qxdzbc.p6.translator.jvm_translator.tree_extractor

import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result
import org.antlr.v4.runtime.tree.ParseTree

/**
 * Extract a ParseTree from a string
 */
interface TreeExtractor{
    fun extractTree(formula: String): Result<ParseTree,ErrorReport>
}

