package com.qxdzbc.p6.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.autocomplete.FormulaAutoCompleter
import com.qxdzbc.p6.translator.autocomplete.FormulaAutoCompleterImp
import com.qxdzbc.p6.translator.cell_range_extractor.CellRangeExtractor
import com.qxdzbc.p6.translator.cell_range_extractor.CellRangePosition
import com.qxdzbc.p6.translator.cell_range_extractor.CellRangeVisitor
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.FunctionMapImp
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitions
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitionsImp
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp
import com.qxdzbc.p6.translator.partial_extrator.PartialFormulaTranslator
import com.qxdzbc.p6.translator.partial_extrator.PartialFormulaTreeExtractor
import com.qxdzbc.p6.translator.partial_extrator.PartialJvmFormulaVisitor
import dagger.Binds
import dagger.Provides

@dagger.Module
interface TranslatorModule {
    @Binds
    @P6Singleton
    @CellRangeExtractor_Qualifier
    fun CellRangeExtractor(i: CellRangeExtractor): CellRangeExtractor
//    fun CellRangeExtractor(i: CellRangeExtractor): P6Translator<List<CellRangePosition>>

    @Binds
    @P6Singleton
    @CellRangeVisitor_Qualifier
    fun CellRangeVisitor(i: CellRangeVisitor): CellRangeVisitor
//    fun CellRangeVisitor(i: CellRangeVisitor): FormulaBaseVisitor<List<CellRangePosition>>

    @Binds
    @P6Singleton
    @PartialTranslator
    fun PartialFormulaTranslator(i: PartialFormulaTranslator): P6Translator<String?>

    @Binds
    @P6Singleton
    @PartialVisitor
    fun PartialJvmFormulaVisitor(i: PartialJvmFormulaVisitor): FormulaBaseVisitor<String?>

    @Binds
    @P6Singleton
    @PartialTreeExtractor
    fun PartialFormulaTreeExtractor(i: PartialFormulaTreeExtractor): TreeExtractor

    @Binds
    @P6Singleton
    fun FormulaAutoCompleter(i: FormulaAutoCompleterImp): FormulaAutoCompleter

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
        @FunctionMapMs
        fun FunctionMapMs(fm: FunctionMap): Ms<FunctionMap> {
            return fm.toMs()
        }

        @Provides
        @P6Singleton
        @FunctionMapSt
        fun FunctionMapSt(i: Ms<FunctionMap>): St<FunctionMap> {
            return i
        }
    }
}
