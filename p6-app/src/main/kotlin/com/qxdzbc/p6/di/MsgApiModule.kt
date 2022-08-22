package com.qxdzbc.p6.di

import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContext
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContextReadOnly
import com.qxdzbc.p6.message.api.connection.kernel_services.KernelServiceManager
import com.qxdzbc.p6.message.di.MessageApiComponent
import dagger.Provides
import org.zeromq.ZContext

@dagger.Module
interface MsgApiModule {
    companion object {
        @Provides
        @P6Singleton
        fun KernelServiceManager(msgApiComponent: MessageApiComponent): KernelServiceManager {
            return msgApiComponent.kernelServiceManager()
        }

        @Provides
        @P6Singleton
        fun ZContext(msgApiComponent: MessageApiComponent):ZContext{
            return msgApiComponent.zContext()
        }

        @Provides
        @P6Singleton
        fun KernelContext(msgApiComponent: MessageApiComponent):KernelContext{
            return msgApiComponent.kernelContext()
        }

        @Provides
        @P6Singleton
        fun KernelContextReadOnly(kernelContext:KernelContext): KernelContextReadOnly {
            return kernelContext
        }
    }
}
