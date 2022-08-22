package com.emeraldblast.p6.di.status_bar

import com.emeraldblast.p6.di.P6Singleton
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.ms
import com.emeraldblast.p6.ui.window.status_bar.StatusBarState
import com.emeraldblast.p6.ui.window.status_bar.StatusBarStateImp
import com.emeraldblast.p6.ui.window.status_bar.kernel_status.KernelStatusItemState
import com.emeraldblast.p6.ui.window.status_bar.kernel_status.KernelStatusItemStateImp
import com.emeraldblast.p6.ui.window.status_bar.rpc_status.RPCStatusItemState
import com.emeraldblast.p6.ui.window.status_bar.rpc_status.RPCStatusItemStateImp
import dagger.Binds
import dagger.Provides

@dagger.Module
interface StatusBarModule {


    @Binds
    fun StatusBarState(i:StatusBarStateImp): StatusBarState

    @Binds
    fun KernelStatusItemState(i: KernelStatusItemStateImp):KernelStatusItemState

    companion object{
        @Provides
        @P6Singleton
        @StatusBarStateQualifier
        fun StatusBarStateMs(i:StatusBarState):Ms<StatusBarState>{
            return ms(i)
        }

        @Provides
        @P6Singleton
        @KernelStatusItemStateQualifier
        fun KernelStatusItemStateMs(i: KernelStatusItemState):Ms<KernelStatusItemState>{
            return ms(i)
        }
        @Provides
        @P6Singleton
        @RPCStatusItemStateQualifier
        fun RPCStatusItemStateMs():Ms<RPCStatusItemState>{
            return ms(RPCStatusItemStateImp())
        }
    }
}
