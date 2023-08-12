package com.qxdzbc.p6.common.table

abstract class AbstractTableCR<C, R, E> : TableCR<C, R, E> {
    override val allColumns: List<TableCRColumn<C, E>>
        get() {
            return dataMap.map { (colIndex,rowMap)->
                TableCRColumnImp(colIndex,rowMap.map { it.value })
            }
        }

    override fun getElement(colKey: C, rowKey: R): E? {
        return dataMap[colKey]?.get(rowKey)
    }

    override val allRows: List<TableCRRow<R, E>>
        get(){
            val rt = mutableMapOf<R,MutableList<E>>()
            dataMap.values.forEach { col->
                col.forEach { (rowIndex,element)->
                   val row = rt[rowIndex] ?:run{
                       val newRow = mutableListOf<E>()
                       rt[rowIndex] = newRow
                       newRow
                   }
                    row.add(element)
                }
            }
            return rt.map{
                TableCRRowImp(it.key,it.value)
            }
        }
}
