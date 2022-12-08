package test.splitter

sealed interface TestContext{
    val testDescription:String
    data class Str(override val testDescription: String) :TestContext
}


abstract class TestSplitter {

//    private fun runOnTestContext(description: String = "", f: TestContext.() -> Unit) {
//        val context=TestContext.Str(description)
//        try{
//            context.f()
//        }catch (e:Throwable){
//            when(e){
//                is AssertionError->
//                    throw AssertionError("fail :${description}",e)
//                else -> throw Exception("exception in test:${description}",e)
//            }
//        }
//    }
//    private fun TestContext.runFromTestContext(description: String = "", f: () -> Unit) {
//        try{
//            f()
//        }catch (e:Throwable){
//            when(e){
//                is AssertionError->
//                    throw AssertionError("fail :${description}",e)
//                else -> throw Exception("exception in test:${description}",e)
//            }
//        }
//    }
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
