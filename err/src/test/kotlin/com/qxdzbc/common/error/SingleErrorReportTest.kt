package com.qxdzbc.common.error

import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.*

internal class SingleErrorReportTest{
    @Test
    fun plus(){
        val e1 = SingleErrorReport.random()
        val e2 = SingleErrorReport.random()
        val e3 = SingleErrorReport.random()
        val em1 = e1.mergeWith(e2)
        em1.shouldBeInstanceOf<MultiErrorReport>()
        em1.singleErrorReportList.shouldContainOnly(e1,e2)

        val em2 = em1.mergeWith(e3)
        em2.shouldBeInstanceOf<MultiErrorReport>()
        em2.singleErrorReportList.shouldContainOnly(e1,e2,e3)
    }
}
