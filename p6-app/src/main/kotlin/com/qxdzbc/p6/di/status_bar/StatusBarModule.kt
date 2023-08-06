package com.qxdzbc.p6.di.status_bar

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.di.status_bar.qualifiers.RPCStatusItemStateQualifier
import com.qxdzbc.p6.di.status_bar.qualifiers.StatusBarStateQualifier
import com.qxdzbc.p6.ui.window.status_bar.StatusBarState
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RPCStatusViewState
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RPCStatusViewStateImp
import dagger.Provides
import javax.inject.Singleton

import dagger.Module
@Module
interface StatusBarModule {

    companion object{
        @Provides
        @Singleton
        @StatusBarStateQualifier
        fun StatusBarStateMs(i:StatusBarState):Ms<StatusBarState>{
            return ms(i)
        }

        @Provides
        @Singleton
        @RPCStatusItemStateQualifier
        fun RPCStatusItemStateMs():Ms<RPCStatusViewState>{
            return ms(RPCStatusViewStateImp())
        }
    }
}
