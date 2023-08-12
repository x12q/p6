package com.qxdzbc.p6.common.table

import com.qxdzbc.p6.document_data_layer.cell.address.CRAddress

/**
 * a 2d table with custom col and row
 * C = type of column key
 * R = type of row key
 * E = type of element
 */
interface TableCR<C, R, E> :Map<C, Map<R, E>> {
    val allElements: List<E>
    val dataMap: Map<C, Map<R, E>>
    val itemCount: Int get() = allElements.size

    val allColumns:List<TableCRColumn<C,E>>
    val allRows:List<TableCRRow<R,E>>

    fun getElement(colKey: C, rowKey: R): E?
    fun getElement(address: CRAddress<C, R>): E? {
        return this.getElement(address.colIndex, address.rowIndex)
    }

    fun remove(colKey: C, rowKey: R): TableCR<C, R, E>
    fun remove(address:CRAddress<C, R>): TableCR<C, R, E> {
        return this.remove(address.colIndex,address.rowIndex)
    }
    fun removeAll(): TableCR<C, R, E>

    fun set(colKey: C, rowKey: R, element: E): TableCR<C, R, E>

    fun set(crAddress: CRAddress<C, R>, element: E): TableCR<C, R, E> {
        return this.set(crAddress.colIndex, crAddress.rowIndex, element)
    }

    fun hasElementAt(colKey: C, rowKey: R): Boolean {
        return getElement(colKey, rowKey) != null
    }

    fun hasElementAt(cellAddress: CRAddress<C, R>): Boolean {
        return hasElementAt(cellAddress.colIndex, cellAddress.rowIndex)
    }

    fun getCol(colKey: C): List<E>
    fun removeCol(colKey: C): TableCR<C, R, E>

    fun getRow(rowKey: R): List<E>
    fun removeRow(rowKey: R): TableCR<C, R, E>

    override fun isEmpty(): Boolean
    fun isNotEmpty(): Boolean = dataMap.isNotEmpty()
}


