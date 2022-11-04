package com.qxdzbc.p6.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.partial_text_element_extractor.PartialTextElementExtractor
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.FunctionMapImp
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitions
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitionsImp
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp
import com.qxdzbc.p6.translator.partial_text_element_extractor.PartialFormulaTreeExtractor
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementVisitor
import dagger.Binds
import dagger.Provides

@dagger.Module
interface TranslatorModule {
    @Binds
    @P6Singleton
    @TextElementVisitor_Qualifier
    fun TextElementVisitor(i: TextElementVisitor): FormulaBaseVisitor<TextElementResult>

    @Binds
    @P6Singleton
    @PartialCellRangeExtractorQ
    fun PartialCellRangeExtractor(i: PartialTextElementExtractor): P6Translator<TextElementResult>

    @Binds
    @P6Singleton
    @PartialTreeExtractor
    fun PartialFormulaTreeExtractor(i: PartialFormulaTreeExtractor): TreeExtractor

    @Binds
    @P6Singleton
    fun TreeExtractor(i: TreeExtractorImp): TreeExtractor

    @Binds
    @P6Singleton
    fun P6FunctionDefs(p6FunctionDef: P6FunctionDefinitionsImp): P6FunctionDefinitions

    companion object {
        @Provides
        fun FunctionMap(fd: P6FunctionDefinitions): FunctionMap {
            return FunctionMapImp(fd.functionMap)
        }

        @Provides
        @P6Singleton
        fun FunctionMapMs(fm: FunctionMap): Ms<FunctionMap> {
            return fm.toMs()
        }

        @Provides
        @P6Singleton
        fun FunctionMapSt(i: Ms<FunctionMap>): St<FunctionMap> {
            return i
        }
    }
}
