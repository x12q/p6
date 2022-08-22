package com.qxdzbc.p6.app.common.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Loggers {
    val scriptLogger: Logger = LoggerFactory.getLogger("scriptLogger")
    val commonLogger:Logger = LoggerFactory.getLogger("commonLogger")
    val serviceLogger:Logger = LoggerFactory.getLogger("repServiceLogger")
    val msgApiCommonLogger:Logger = LoggerFactory.getLogger("msgApiCommonLogger")
    val renderLogger:Logger = LoggerFactory.getLogger("renderLogger")
}


