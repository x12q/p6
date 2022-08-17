package test


class TestScope{
    fun expect(f:()->Unit){
        f()
    }
    fun step(description: String="",f:TestScope.()->Unit = {}){
        f()
    }
}
fun case(description:String="",f:TestScope.()->Unit = {}){
    val o = TestScope()
    f(o)
}


