package com.qxdzbc.p6.composite_actions.app.load_wb

import com.qxdzbc.common.path.PPath
import com.qxdzbc.common.path.PPaths
import com.qxdzbc.p6.rpc.communication.res_req_template.request.RequestWithWindowId
import com.qxdzbc.p6.proto.AppProtos.LoadWorkbookRequestProto
import java.nio.file.Path
import kotlin.io.path.absolutePathString

data class LoadWorkbookRequest(val path: PPath, override val windowId:String?) : RequestWithWindowId {
    fun toProto(): LoadWorkbookRequestProto {
        return LoadWorkbookRequestProto.newBuilder()
            .setPath(this.path.path.absolutePathString())
            .build()
    }

    companion object{
        fun LoadWorkbookRequestProto.toModel():LoadWorkbookRequest{
            return LoadWorkbookRequest(PPaths.get(Path.of(path)), null)
        }
    }
}
