package com.emeraldblast.p6.app.common.utils

import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isReadable
import kotlin.io.path.isRegularFile

/**
 * An interface wrap around Path. The reason is that it is very hard to mock Path directly
 */
interface PPath {
    fun isRegularFile():Boolean
    fun isReadable():Boolean
    fun exists():Boolean
    fun toAbsolutePath():PPath
    val path:Path
}

fun PPath(path: Path):PPathImp = PPathImp(path)

data class PPathImp(override val path: Path):PPath{
    override fun isRegularFile(): Boolean {
        return path.isRegularFile()
    }

    override fun isReadable(): Boolean {
        return path.isReadable()
    }

    override fun exists(): Boolean {
        return path.exists()
    }

    override fun toAbsolutePath(): PPathImp {
        return this.copy(path = path.toAbsolutePath())
    }

    override fun toString(): String {
        return this.path.toString()
    }
}
