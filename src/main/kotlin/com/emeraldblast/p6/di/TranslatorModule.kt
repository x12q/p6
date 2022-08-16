package com.emeraldblast.p6.di

import com.emeraldblast.p6.translator.formula.FunctionMap
import com.emeraldblast.p6.translator.formula.FunctionMapImp
import com.emeraldblast.p6.translator.formula.P6FunctionDefinitions
import com.emeraldblast.p6.translator.formula.P6FunctionDefinitionsImp
import com.emeraldblast.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.emeraldblast.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp
import dagger.Binds
import dagger.Provides

@dagger.Module
interface TranslatorModule {
    @Binds
    @P6Singleton
    fun TreeExtractor(i:TreeExtractorImp): TreeExtractor

    @Binds
    @P6Singleton
    fun P6FunctionDefs(p6FunctionDef: P6FunctionDefinitionsImp): P6FunctionDefinitions

    companion object {
        @Provides
        @P6Singleton
        fun FunctionMap(fd: P6FunctionDefinitions): FunctionMap {
            return FunctionMapImp(fd.functionMap)
        }
    }
}
