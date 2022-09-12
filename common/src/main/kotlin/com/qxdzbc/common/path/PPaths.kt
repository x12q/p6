package com.qxdzbc.common.path

import com.qxdzbc.common.path.PPathImp
import java.nio.file.Path

object PPaths {
    fun get(path:Path):PPath = PPathImp(path)
}
