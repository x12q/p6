package com.qxdzbc.p6.di

import com.qxdzbc.common.copiers.binary_copier.BinaryCopier
import com.qxdzbc.common.copiers.binary_copier.BinaryCopierImp
import com.qxdzbc.p6.app.document.workbook.WorkbookFactory
import com.qxdzbc.p6.app.document.workbook.AutoNameWbFactory
import com.qxdzbc.p6.app.document.worksheet.WsNameGenerator
import com.qxdzbc.p6.app.document.worksheet.WsNameGeneratorImp
import com.qxdzbc.p6.app.file.loader.P6FileLoader
import com.qxdzbc.p6.app.file.loader.P6FileLoaderImp
import com.qxdzbc.p6.app.file.saver.P6Saver
import com.qxdzbc.p6.app.file.saver.P6SaverImp
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParser
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParserImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.nio.file.Files
import java.nio.file.Path

@Module
interface UtilModule {
//    @Binds
//    @P6Singleton
//    fun CellLiteralParser(i:CellLiteralParserImp): CellLiteralParser

//    @Binds
//    @P6Singleton
//    fun P6Saver(i:P6SaverImp): P6Saver

//    @Binds
//    @P6Singleton
//    fun P6Loader(i:P6FileLoaderImp):P6FileLoader

//    @Binds
//    @P6Singleton
//    fun WsNameGenerator(i:WsNameGeneratorImp): WsNameGenerator

//    @Binds
//    @P6Singleton
//    fun WorkbookFactory(i: AutoNameWbFactory):WorkbookFactory


    companion object{

        @Provides
        @P6Singleton
        fun BinaryCopier(): BinaryCopier{
            return BinaryCopierImp()
        }

        @Provides
        @P6Singleton
        @UtilQualifier.ReadFileFunction
        fun readFileFunction():(path: Path)->ByteArray{
            return Files::readAllBytes
        }
    }
}
