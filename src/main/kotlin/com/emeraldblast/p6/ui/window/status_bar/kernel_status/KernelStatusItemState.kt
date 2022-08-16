package com.emeraldblast.p6.ui.window.status_bar.kernel_status

interface KernelStatusItemState {
    val detailIsShown:Boolean
    fun showDetail():KernelStatusItemState
    fun hideDetail():KernelStatusItemState
    val kernelIsRunning:Boolean
    val kernelIsUnderManagement:Boolean
    val kernelCommand:String?
    val connectionFilePath:String?
    val kernelProcessId: Long?
    val shellPort:Int?
    val ioPubPort:Int?
    val stdinPort:Int?
    val heartBeatPort:Int?
    val ip:String?
}
