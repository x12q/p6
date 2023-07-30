package com.qxdzbc.p6.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.partial_text_element_extractor.PartialTextElementTranslator
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
import javax.inject.Singleton

import dagger.Module
@Module
interface TranslatorModule {

    @Binds
    @Singleton
    @TextElementVisitorQ
    fun TextElementVisitor(i: TextElementVisitor): FormulaBaseVisitor<TextElementResult>

    @Binds
    @Singleton
    @PartialCellRangeExtractorQ
    fun PartialCellRangeExtractor(i: PartialTextElementTranslator): P6Translator<TextElementResult>

    @Binds
    @Singleton
    @PartialTreeExtractor
    fun PartialFormulaTreeExtractor(i: PartialFormulaTreeExtractor): TreeExtractor

    @Binds
    @Singleton
    fun TreeExtractor(i: TreeExtractorImp): TreeExtractor

    @Binds
    @Singleton
    fun P6FunctionDefs(p6FunctionDef: P6FunctionDefinitionsImp): P6FunctionDefinitions

    companion object {
        @Provides
        fun FunctionMap(fd: P6FunctionDefinitions): FunctionMap {
            return FunctionMapImp(fd.functionMap)
        }

        @Provides
        @Singleton
        fun FunctionMapMs(fm: FunctionMap): Ms<FunctionMap> {
            return fm.toMs()
        }

        @Provides
        @Singleton
        fun FunctionMapSt(i: Ms<FunctionMap>): St<FunctionMap> {
            return i
        }
    }
}
