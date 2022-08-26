package com.qxdzbc.p6.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.FunctionMapImp
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitions
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitionsImp
import com.qxdzbc.p6.translator.formula.function_def.formula_back_converter.FunctionFormulaBackConverter
import com.qxdzbc.p6.translator.formula.function_def.formula_back_converter.FunctionFormulaBackConverterNormal
import com.qxdzbc.p6.translator.formula.function_def.formula_back_converter.FunctionFormulaBackConverter_ForGetCell
import com.qxdzbc.p6.translator.formula.function_def.formula_back_converter.FunctionFormulaBackConverter_ForGetRangeAddress
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp
import dagger.Binds
import dagger.Provides

@dagger.Module
interface TranslatorModule {

    @Binds
    @P6Singleton
    @BackConverterForGetCell
    fun f3(i: FunctionFormulaBackConverter_ForGetCell): FunctionFormulaBackConverter

    @Binds
    @P6Singleton
    @BackConverterForGetRange
    fun f1(i: FunctionFormulaBackConverter_ForGetRangeAddress): FunctionFormulaBackConverter

    @Binds
    @P6Singleton
    @NormalBackConverter
    fun f2(i: FunctionFormulaBackConverterNormal): FunctionFormulaBackConverter

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
