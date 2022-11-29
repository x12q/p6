package com.qxdzbc.p6.app.common.table


/**
 * Generic 2d Table
 */
class MutableTableCR<C, R, E>(
    override val dataMap: MutableMap<C, MutableMap<R, E>> = mutableMapOf(),
) : AbstractTableCR<C, R, E>(), Map<C, Map<R, E>> by dataMap{

//    override fun getElement(colKey: C, rowKey: R): E? {
//        return dataMap[colKey]?.get(rowKey)
//    }

    override fun remove(colKey: C, rowKey: R): MutableTableCR<C, R, E> {
        dataMap[colKey]?.remove(rowKey)
        if (dataMap[colKey]?.isEmpty() == true) {
            dataMap.remove(colKey)
        }
        return this
    }

    override fun set(colKey: C, rowKey: R, element: E): MutableTableCR<C, R, E> {
        dataMap.putIfAbsent(colKey, mutableMapOf())
        dataMap[colKey]?.put(rowKey, element)
        return this
    }

    override fun getCol(colKey: C): List<E> {
        return dataMap[colKey]?.values?.toList() ?: emptyList()
    }

    override fun removeCol(colKey: C): MutableTableCR<C, R, E> {
        dataMap.remove(colKey)
        return this
    }

    override fun getRow(rowKey: R): List<E> {
        val rt = mutableListOf<E>()
        dataMap.map { (_, m) ->
            val e = m[rowKey]
            if (e != null) {
                rt.add(e)
            }
        }
        return rt
    }

    override fun removeRow(rowKey: R): MutableTableCR<C, R, E> {
        dataMap.map { (_, m) ->
            m.remove(rowKey)
        }
        return this
    }

    override val allElements: List<E>
        get() {
            val rt: List<E> = dataMap.flatMap { (_, m) -> m.values }
            return rt
        }

    override fun removeAll(): TableCR<C, R, E> {
        this.dataMap.clear()
        return this
    }
}
