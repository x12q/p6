package com.qxdzbc.p6.ui.format.attr

enum class BoolAttr {
    TRUE {
        override val boolean: Boolean = true
    },FALSE {
        override val boolean: Boolean = false
    };
    companion object{
        fun Boolean.toBoolAttr():BoolAttr{
            return if(this) TRUE else FALSE
        }
    }
    abstract val boolean:Boolean
}
