package com.emeraldblast.p6.app.document.cell.d

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.address.CellAddresses
import com.emeraldblast.p6.app.document.cell.d.CellValue.Companion.toCellValue

object Cells {
    fun empty(colIndex: Int, rowIndex: Int): Cell {
        return empty(CellAddresses.fromIndices(colIndex, rowIndex))
    }

    fun empty(label: String): Cell {
        return empty(CellAddresses.fromLabel(label))
    }

    fun number(label: String, number: Number): Cell {
        return empty(label).setCellValue(number.toCellValue())
    }

    fun str(label: String, str: String): Cell {
        return empty(label).setCellValue(str.toCellValue())
    }

    fun empty(cellAddress: CellAddress): Cell {
        return CellImp(
            cellAddress,
            CellContentImp()
        )
    }
}
