package com.emeraldblast.p6.app.common.table

import com.emeraldblast.p6.app.document.cell.address.GenericCellAddress

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

    fun getElement(colKey: C, rowKey: R): E?
    fun getElement(address: GenericCellAddress<C, R>): E? {
        return this.getElement(address.colIndex, address.rowIndex)
    }

    fun remove(colKey: C, rowKey: R): TableCR<C, R, E>
    fun remove(address:GenericCellAddress<C, R>): TableCR<C, R, E> {
        return this.remove(address.colIndex,address.rowIndex)
    }
    fun removeAll(): TableCR<C, R, E>

    fun set(colKey: C, rowKey: R, element: E): TableCR<C, R, E>

    fun set(cellAddress: GenericCellAddress<C, R>, element: E): TableCR<C, R, E> {
        return this.set(cellAddress.colIndex, cellAddress.rowIndex, element)
    }

    fun hasElementAt(colKey: C, rowKey: R): Boolean {
        return getElement(colKey, rowKey) != null
    }

    fun hasElementAt(cellAddress: GenericCellAddress<C, R>): Boolean {
        return hasElementAt(cellAddress.colIndex, cellAddress.rowIndex)
    }

    fun getCol(colKey: C): List<E>
    fun removeCol(colKey: C): TableCR<C, R, E>

    fun getRow(rowKey: R): List<E>
    fun removeRow(rowKey: R): TableCR<C, R, E>

    override fun isEmpty(): Boolean
    fun isNotEmpty(): Boolean = dataMap.isNotEmpty()
}


fun <C, R, E> TableCR<C, R, E>.toMutable(): TableCR<C, R, E> {
    if (this is MutableTableCR) {
        return this
    } else {
        val mdata = mutableMapOf<C, MutableMap<R, E>>()
        for ((k0, m) in this.dataMap) {
            mdata[k0] = m.toMutableMap()
        }
        return MutableTableCR(mdata)
    }
}

fun <C, R, E> TableCR<C, R, E>.toImmutable(): TableCR<C, R, E> {
    if (this is ImmutableTableCR) {
        return this
    }
    return ImmutableTableCR(this.dataMap)
}
