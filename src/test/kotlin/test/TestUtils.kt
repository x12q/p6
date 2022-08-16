package test

object TestUtils {
    fun <T> compare2ListIgnoreOrder(l1:Collection<T>,l2:Collection<T>):Boolean{
        return l1.size == l2.size && l1.containsAll(l2) && l2.containsAll(l1)
    }

}
