package com.qxdzbc.p6.app.common.utils

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St

object TypeUtils {
    inline fun <reified T>Any?.checkStAndCast():St<T>?{
        if(this is St<*>){
            val v = this.value
            if(v!=null){
                if (v is T){
                    return this as St<T>
                }else{
                    return null
                }
            }else{
                return null
            }
        }else{
            return null
        }
    }

    inline fun <reified T>Any?.checkMsAndCast():Ms<T>?{
        if(this is Ms<*>){
            val v = this.value
            if(v!=null){
                if (v is T){
                    return this as Ms<T>
                }else{
                    return null
                }
            }else{
                return null
            }
        }else{
            return null
        }
    }

}
