package com.qxdzbc.common.error


import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

internal class MultiErrorReportTest {

    @Test
    fun plus() {
        val es1 = SingleErrorReport.random()
        val es2 = SingleErrorReport.random()
        val es3 = SingleErrorReport.random()
        val e1 = MultiErrorReport(header = ErrorHeader.random(), listOf(es1))
        e1.singleErrorReportList.shouldContainOnly(es1)
        val e2 = e1 + es2 + es3
        e2.shouldBeInstanceOf<MultiErrorReport>()
        e2.singleErrorReportList.shouldContainOnly(es1, es2, es3)
    }
}
