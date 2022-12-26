package com.qxdzbc.common.test_util


abstract class TestSplitter {

    /**
     * just to make long tests easier to read
     */
    fun test(description: String = "", run:Boolean=true, f: () -> Unit) {
        if(run){
            f()
        }
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
