package com.qxdzbc.p6.di.status_bar

import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.window.status_bar.StatusBarState
import com.qxdzbc.p6.ui.window.status_bar.StatusBarStateImp
import com.qxdzbc.p6.ui.window.status_bar.kernel_status.KernelStatusItemState
import com.qxdzbc.p6.ui.window.status_bar.kernel_status.KernelStatusItemStateImp
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RPCStatusViewState
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RPCStatusViewStateImp
import dagger.Binds
import dagger.Provides

@dagger.Module
interface StatusBarModule {


    @Binds
    fun StatusBarState(i:StatusBarStateImp): StatusBarState

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
        fun RPCStatusItemStateMs():Ms<RPCStatusViewState>{
            return ms(RPCStatusViewStateImp())
        }
    }
}
