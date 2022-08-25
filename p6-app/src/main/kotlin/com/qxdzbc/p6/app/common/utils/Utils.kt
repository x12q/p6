package com.qxdzbc.p6.app.common.utils

import io.grpc.stub.StreamObserver
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.ServerSocket

/**
 * This object houses common utility functions that operate on base types such as Boolean, List
 */
object Utils {

    fun <T> StreamObserver<T>.onNextAndComplete(t: T) {
        this.onNext(t)
        this.onCompleted()
    }

    /**
     * Convert a boolean to "Yes"(true) or "No"(false)
     */
    fun Boolean.toYN(): String {
        if (this) {
            return "Yes"
        } else {
            return "No"
        }
    }

    fun Boolean.toTF(): String {
        if (this) {
            return "TRUE"
        } else {
            return "FALSE"
        }
    }

    fun readResource(path: String): String? {
        return this.javaClass.getResource(path)?.readText()
    }

    /**
     * Find an available socket port
     */
    fun findSocketPort(): Int {
        val socket = ServerSocket(0)
        val port = socket.localPort
        socket.close()
        return port
    }

    /**
     * copy a string to clipboard
     */
    fun copyTextToClipboard(text: String) {
        Toolkit.getDefaultToolkit().systemClipboard
            .setContents(StringSelection(text), null)
    }

}
