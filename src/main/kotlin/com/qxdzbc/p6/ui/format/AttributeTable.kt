package com.qxdzbc.p6.ui.format

import com.qxdzbc.p6.app.common.table.TableCR
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute
import com.qxdzbc.p6.ui.format.pack.AttributePack


/**
 * TODO attribute now is fairly good for adding attribute but not good for removing attribute.
 * Actually, this attribute table is terrible.
 * Eg: changing font from 10 to 12. This involve removing font 10 attribute, then add attribute 12.
 */
interface AttributeTable {

    val table: TableCR<Int, Int, AttributePack>
    val markedAttributes: Set<MarkedAttribute>

    /**
     * Add an attribute to a cell at [col]:[row] address
     */
    fun add(col: Int, row: Int, attr: FormatAttribute): AttributeTable

    /**
     * remove an attribute from ALL cells
     */
    fun removeAttrFromAllCell(attr: FormatAttribute): AttributeTable

    /**
     * remove a particular attribute from a cell at [col]:[row] address
     */
    fun removeAttrFromOneCell(col: Int, row: Int, attr: FormatAttribute): AttributeTable

    /**
     * Remove all attributes at [col]:[row] address
     */
    fun removeAllAttrAt(col: Int, row: Int): AttributeTable

    /**
     * get the composite attribute at [col]:[row] address
     */
    fun getAttr(col: Int, row: Int): FormatAttribute?
    fun getAttr(cellAddress: CellAddress): FormatAttribute? {
        return this.getAttr(cellAddress.colIndex, cellAddress.rowIndex)
    }

    fun getAttrPack(col: Int, row: Int): AttributePack?
    fun getAttrPack(cellAddress: CellAddress): AttributePack? {
        return this.getAttrPack(cellAddress.colIndex,cellAddress.rowIndex)
    }
}

