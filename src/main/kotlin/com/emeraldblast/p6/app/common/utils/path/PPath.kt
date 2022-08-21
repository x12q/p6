package com.emeraldblast.p6.app.common.utils.path

import java.nio.file.Path

/**
 * An interface wrap around Path. The reason is that it is very hard to mock Path directly
 */
interface PPath {
    fun isRegularFile():Boolean
    fun isReadable():Boolean
    fun exists():Boolean
    fun toAbsolutePath(): PPath
    val path:Path
}
