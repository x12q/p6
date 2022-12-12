package com.qxdzbc.p6.app.action.app.load_wb

import com.qxdzbc.common.path.PPath
import com.qxdzbc.common.path.PPaths
import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWindowId
import com.qxdzbc.p6.proto.AppProtos.LoadWorkbookRequestProto
import java.nio.file.Path
import kotlin.io.path.absolutePathString

data class LoadWorkbookRequest(val path: PPath, override val windowId:String?) : RequestToP6WithWindowId {
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
