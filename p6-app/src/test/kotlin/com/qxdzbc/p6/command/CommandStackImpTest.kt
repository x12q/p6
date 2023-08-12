package com.qxdzbc.p6.command

import com.qxdzbc.p6.command.Command
import com.qxdzbc.p6.command.CommandStackImp
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.mock
import kotlin.test.*

internal class CommandStackImpTest {
    var _stack: CommandStackImp?=null
    val stack get()=_stack!!
    @BeforeTest
    fun b(){
        _stack = CommandStackImp(3)
    }
    fun newCommand(): Command {
        return mock<Command>()
    }
    @Test
    fun `add, peek, removeTop`() {
        val c1 = newCommand()
        val c2 = newCommand()
        val s2 = stack.add(c1)
        (c1 in s2) shouldBe true
        s2.peek() shouldBe c1
        val s3 = s2.add(c2)
        s3.peek() shouldBe c2
        val s4 = s3.removeTop()
        s4.peek() shouldBe c1
    }

    @Test
    fun `size constraint`() {
        val c1 = newCommand()
        val c2 = newCommand()
        val c3 = newCommand()
        val c4 = newCommand()
        val c5 = newCommand()
        val s1 = stack.add(c1).add(c2).add(c3)
        val s2 = s1.add(c4)
        s2.peek() shouldBe c4
        s2.size shouldBe 3
        s2.allCommands.shouldContainAll(c4,c3,c2)
        s2.allCommands.shouldContainOnly(c4,c3,c2)

        val s3 = s2.add(c5)
        s3.peek() shouldBe c5
        s3.size shouldBe 3
        s3.allCommands.shouldContainInOrder(c5,c4,c3)
        s3.allCommands.shouldContainOnly(c5,c4,c3)
    }
}
