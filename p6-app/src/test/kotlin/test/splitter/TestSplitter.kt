package test.splitter

sealed interface TestContext{
    val testDescription:String
    data class Str(override val testDescription: String) :TestContext
}


abstract class TestSplitter {

    /**
     * just to make long tests easier to read
     */
    fun test(description: String = "", f: () -> Unit) {
        f()
    }

    /**
     * just to make long tests easier to read
     */
    fun preCondition(description: String = "", f: () -> Unit) {
        f()
    }

    /**
     * just to make long tests easier to read
     */
    fun postCondition(description: String = "", f: () -> Unit) {
        f()
    }

    /**
     * just to make long tests easier to read
     */
    fun setup(description: String = "", f: () -> Unit) {
        f()
    }
}
