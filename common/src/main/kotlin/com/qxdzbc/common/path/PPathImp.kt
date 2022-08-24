package com.qxdzbc.common.path

import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isReadable
import kotlin.io.path.isRegularFile

data class PPathImp(override val path: Path): PPath {

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
