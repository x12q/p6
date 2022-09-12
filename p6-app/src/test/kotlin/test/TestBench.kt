package test
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.test.*
class TestBench {
    @Test
    fun t(){
        val d = LocalDateTime.now()
        val i = d.toEpochSecond(ZoneOffset.UTC)
        println(i)
        val d2=LocalDateTime.ofEpochSecond(i,0, ZoneOffset.UTC)

    }
}
