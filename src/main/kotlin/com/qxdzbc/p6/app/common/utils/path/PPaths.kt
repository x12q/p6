package com.qxdzbc.p6.app.common.utils.path

import java.nio.file.Path

object PPaths {
    fun PPath(path: Path): PPathImp = PPathImp(path)
}
