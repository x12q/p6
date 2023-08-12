package com.qxdzbc.p6.rpc.communication.res_req_template

interface IsError {
    val isError:Boolean
    val isOk:Boolean get()=!isError
}
