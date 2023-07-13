package com.qxdzbc.p6.app.file.saver

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto
import com.qxdzbc.p6.proto.P6FileProtos.*
import com.qxdzbc.p6.ui.document.workbook.state.CanConvertToWorkbookProto
import com.squareup.anvil.annotations.ContributesBinding
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class P6SaverImp @Inject constructor() : P6Saver {
    override fun saveAsProtoBuf(wb: CanConvertToWorkbookProto, path: Path): Rse<Unit> {
        try {
            val proto: WorkbookProto = wb.toProto()
            val date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            val fileContent = P6FileContentProto.newBuilder()
                .setMeta(
                    P6FileMetaInfoProto.newBuilder()
                        .setDate(date).build()
                ).setWorkbook(proto)
                .build()
            val fileProto = P6FileProto.newBuilder()
                .setVersion("1")
                .setContent(fileContent.toByteString())
                .build()
            Files.write(path, fileProto.toByteString().toByteArray())
            return Ok(Unit)
        } catch (e: Throwable) {
            return Err(CommonErrors.ExceptionError.report(e))
        }
    }

    override fun saveFirstWsAsCsv(wb: Workbook, path: Path): Rse<Unit> {
        try {
            wb.worksheets.firstOrNull()?.also { ws ->
                val minRow = ws.usedRowRange.first
                val maxCol = ws.usedColRange.last
                val lineMap = mutableMapOf<Int,List<String?>>()
                for(row in ws.allRows){
                    val line = if (row.isNotEmpty()) {
                        val sortedCells = row.sortedBy { it.address.colIndex }
                        var cellIndex = 0
                        val rowTexts = (1 .. maxCol).map { i ->
                            if (sortedCells.getOrNull(cellIndex)?.address?.colIndex == i) {
                                val str = sortedCells[cellIndex].currentValueAsCsvStr
                                cellIndex++
                                str
                            } else {
                                // x: empty element padding
                                null
                            }
                        }
                        rowTexts
                    } else {
                        emptyList()
                    }
                    lineMap[row.rowIndex] = line
                }

                val writer: BufferedWriter = Files.newBufferedWriter(path)
                CSVPrinter(writer, CSVFormat.DEFAULT).use { csvPrinter ->
                    val completelyEmptyRow:List<String?> = List(maxCol){null}

                    // x: leading padding
                    (1 until minRow).forEach {
                        csvPrinter.printRecord(completelyEmptyRow)
                    }

                    val sortedLineMap = lineMap.toSortedMap()
                    var trackedRowIndex = minRow
                    for((rowIndex,line) in sortedLineMap){
                        val indexDif = rowIndex - trackedRowIndex - 1
                        if(indexDif>0){
                            repeat(indexDif){
                                csvPrinter.printRecord(completelyEmptyRow)
                            }
                            trackedRowIndex = rowIndex
                        }
                        csvPrinter.printRecord(line)
                    }
                }
            }
            return Ok(Unit)
        } catch (e: Throwable) {
            return Err(CommonErrors.ExceptionError.report(e))
        }
    }
}
