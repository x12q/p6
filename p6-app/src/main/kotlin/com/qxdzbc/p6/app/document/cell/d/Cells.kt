package com.qxdzbc.p6.app.document.cell.d

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.cell.d.CellValue.Companion.toCellValue

object Cells {
    fun emptyIndCell(colIndex: Int, rowIndex: Int): Cell {
        return emptyIndCell(CellAddresses.fromIndices(colIndex, rowIndex))
    }

    fun emptyIndCell(label: String): Cell {
        return emptyIndCell(CellAddresses.fromLabel(label))
    }

    fun number(label: String, number: Number): Cell {
        return emptyIndCell(label).setCellValue(number.toCellValue())
    }

    fun str(label: String, str: String): Cell {
        return emptyIndCell(label).setCellValue(str.toCellValue())
    }

    fun emptyIndCell(cellAddress: CellAddress): Cell {
        return IndCellImp(
            cellAddress,
            CellContentImp()
        )
    }
}
