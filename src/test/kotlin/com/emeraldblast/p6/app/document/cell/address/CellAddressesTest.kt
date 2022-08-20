package com.emeraldblast.p6.app.document.cell.address

import com.github.michaelbull.result.Ok
import kotlin.test.*

internal class CellAddressesTest {

    @Test
    fun `create cell address from label`() {
        val inputMap = mapOf(
            "A1" to CellAddressImp(1, 1, false, false),
            "a1" to CellAddressImp(1, 1, false, false),
            "\$A1" to CellAddressImp(1, 1, true, false),
            "\$a1" to CellAddressImp(1, 1, true, false),
            "\$A$1" to CellAddressImp(1, 1, true, true),
            "\$a$1" to CellAddressImp(1, 1, true, true),

            "RR123" to CellAddressImp(486,123),
            "rr123" to CellAddressImp(486,123),
            "RR$123" to CellAddressImp(486,123,false,true),
            "\$RR$123" to CellAddressImp(486,123,true,true),
        )

        for ((l, c) in inputMap) {
            val rs = CellAddresses.fromLabelRs(l)
            assertTrue(rs is Ok)
            assertEquals(c, rs.component1())
        }
    }
}
