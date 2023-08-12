package com.qxdzbc.p6.di

import com.qxdzbc.common.copiers.binary_copier.BinaryCopier
import com.qxdzbc.common.copiers.binary_copier.BinaryCopierImp
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookFactory
import com.qxdzbc.p6.document_data_layer.workbook.AutoNameWbFactory
import com.qxdzbc.p6.document_data_layer.worksheet.WsNameGenerator
import com.qxdzbc.p6.document_data_layer.worksheet.WsNameGeneratorImp
import com.qxdzbc.p6.file.loader.P6FileLoader
import com.qxdzbc.p6.file.loader.P6FileLoaderImp
import com.qxdzbc.p6.file.saver.P6Saver
import com.qxdzbc.p6.file.saver.P6SaverImp
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParser
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParserImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Singleton

@Module
interface UtilModule {

    companion object{

        @Provides
        @Singleton
        fun BinaryCopier(): BinaryCopier{
            return BinaryCopierImp()
        }

        @Provides
        @Singleton
        @UtilQualifier.ReadFileFunction
        fun readFileFunction():(path: Path)->ByteArray{
            return Files::readAllBytes
        }
    }
}
