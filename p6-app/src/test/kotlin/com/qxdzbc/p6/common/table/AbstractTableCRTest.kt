package com.qxdzbc.p6.common.table

import io.kotest.matchers.shouldBe
import kotlin.test.*

internal class AbstractTableCRTest{
    lateinit var table:AbstractTableCR<Int,Int,Int>
    @BeforeTest
    fun b(){
        table = object : AbstractTableCR<Int,Int,Int>() {
            override val allElements: List<Int>
                get() = TODO("Not yet implemented")
            override val dataMap: Map<Int, Map<Int, Int>>
                get() = mapOf(
                    1 to mapOf(
                        1 to 1,
                        2 to 2,
                        3 to 3,
                    ),
                    2 to mapOf(
                        2 to 2,
                        3 to 3,
                    ),
                    3 to mapOf(
                        3 to 3,
                    )
                )

            override fun removeAll(): TableCR<Int, Int, Int> {
                TODO("Not yet implemented")
            }

            override fun isEmpty(): Boolean {
                TODO("Not yet implemented")
            }

            override fun removeRow(rowKey: Int): TableCR<Int, Int, Int> {
                TODO("Not yet implemented")
            }

            override fun getRow(rowKey: Int): List<Int> {
                TODO("Not yet implemented")
            }

            override fun removeCol(colKey: Int): TableCR<Int, Int, Int> {
                TODO("Not yet implemented")
            }

            override fun getCol(colKey: Int): List<Int> {
                TODO("Not yet implemented")
            }

            override fun set(colKey: Int, rowKey: Int, element: Int): TableCR<Int, Int, Int> {
                TODO("Not yet implemented")
            }

            override fun remove(colKey: Int, rowKey: Int): TableCR<Int, Int, Int> {
                TODO("Not yet implemented")
            }

            override val entries: Set<Map.Entry<Int, Map<Int, Int>>>
                get() = TODO("Not yet implemented")
            override val keys: Set<Int>
                get() = TODO("Not yet implemented")
            override val size: Int
                get() = TODO("Not yet implemented")
            override val values: Collection<Map<Int, Int>>
                get() = TODO("Not yet implemented")

            override fun get(key: Int): Map<Int, Int>? {
                TODO("Not yet implemented")
            }

            override fun containsValue(value: Map<Int, Int>): Boolean {
                TODO("Not yet implemented")
            }

            override fun containsKey(key: Int): Boolean {
                TODO("Not yet implemented")
            }

        }

    }

    @Test
    fun allRows(){
        val rows = table.allRows
        rows shouldBe listOf(
            listOf(1),
            listOf(2,2),
            listOf(3,3,3)
        )
    }
}
