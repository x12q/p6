package com.emeraldblast.p6.app.communication.event

import kotlin.reflect.KClass

interface WithP6EventLookupClazz{
    val p6EventLookupClazz: KClass<out Any>
}
