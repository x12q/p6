package com.emeraldblast.p6.di

import com.emeraldblast.p6.app.common.utils.binary_copier.BinaryCopier
import com.emeraldblast.p6.app.common.utils.binary_copier.BinaryCopierImp
import com.emeraldblast.p6.app.document.workbook.WorkbookFactory
import com.emeraldblast.p6.app.document.workbook.AutoNameWbFactory
import com.emeraldblast.p6.app.document.worksheet.WsNameGenerator
import com.emeraldblast.p6.app.document.worksheet.WsNameGeneratorImp
import com.emeraldblast.p6.app.file.loader.P6FileLoader
import com.emeraldblast.p6.app.file.loader.P6FileLoaderImp
import com.emeraldblast.p6.app.file.saver.P6Saver
import com.emeraldblast.p6.app.file.saver.P6SaverImp
import com.emeraldblast.p6.translator.jvm_translator.CellLiteralParser
import com.emeraldblast.p6.translator.jvm_translator.CellLiteralParserImp
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface UtilModule {
    @Binds
    @P6Singleton
    fun CellLiteralParser(i:CellLiteralParserImp): CellLiteralParser

    @Binds
    @P6Singleton
    fun BinaryCopier(i:BinaryCopierImp): BinaryCopier

    @Binds
    @P6Singleton
    fun P6Saver(i:P6SaverImp): P6Saver

    @Binds
    @P6Singleton
    fun P6Loader(i:P6FileLoaderImp):P6FileLoader

    @Binds
    @P6Singleton
    fun WsNameGenerator(i:WsNameGeneratorImp): WsNameGenerator

    @Binds
    @P6Singleton
    fun WorkbookFactory(i: AutoNameWbFactory):WorkbookFactory


    companion object{
        @Provides
        @P6Singleton
        fun gson(): Gson {
            return Gson()
        }
    }
}
