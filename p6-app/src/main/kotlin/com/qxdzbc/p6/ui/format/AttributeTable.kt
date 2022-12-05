package com.qxdzbc.p6.ui.format

import com.qxdzbc.p6.app.common.table.TableCR
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute
import com.qxdzbc.p6.ui.format.pack.AttributePack


/**
 * TODO attribute now is fairly good for adding attribute but not good for removing attribute.
 * Actually, this attribute table is terrible.
 * Eg: changing font from 10 to 12. This involves removing font 10 attribute, then add attribute 12.
 */
interface AttributeTable {

    /**
     * A table that map col-row coor to attribute packs
     */
    val table: TableCR<Int, Int, AttributePack>

    /**
     * A set of all available marked attribute
     */
    val markedAttributes: Set<MarkedAttribute>

    /**
     * Add an attribute to a cell at [col]:[row] address
     */
    fun add(col: Int, row: Int, attr: FormatAttribute): AttributeTable

    /**
     * Add an attribute to a cell at a cell address
     */
    fun add(cellAddress: CellAddress, attr: FormatAttribute): AttributeTable

    /**
     * remove an attribute from ALL cells
     */
    fun removeAttrFromAllCell(attr: FormatAttribute): AttributeTable

    /**
     * remove a particular attribute from a cell at [col]:[row] address
     */
    fun removeOneAttrFromOneCell(col: Int, row: Int, attr: FormatAttribute): AttributeTable

    /**
     * Remove all attributes at [col]:[row] address
     */
    fun removeAllAttrFromOneCell(col: Int, row: Int): AttributeTable

    /**
     * get the composite attribute at [col]:[row] address
     */
    fun getAttr(col: Int, row: Int): FormatAttribute?

    /**
     * get an attribute at a cell address
     */
    fun getAttr(cellAddress: CellAddress): FormatAttribute? {
        return this.getAttr(cellAddress.colIndex, cellAddress.rowIndex)
    }

    /**
     * get an attribute pack at a col:row address
     */
    fun getAttrPack(col: Int, row: Int): AttributePack?

    /**
     * get an attribute pack at a cell address
     */
    fun getAttrPack(cellAddress: CellAddress): AttributePack? {
        return this.getAttrPack(cellAddress.colIndex,cellAddress.rowIndex)
    }
}

