package com.emeraldblast.p6.app.communication.res_req_template

interface IsError {
    val isError:Boolean
    val isOk:Boolean get()=!isError
}
