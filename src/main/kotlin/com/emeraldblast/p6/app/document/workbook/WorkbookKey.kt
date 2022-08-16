package com.emeraldblast.p6.app.document.workbook

import com.emeraldblast.p6.proto.DocProtos
import com.emeraldblast.p6.proto.DocProtos.WorkbookKeyProto
import java.nio.file.Path

data class WorkbookKey(
    val name:String,
    val path:Path? = null
) {
    companion object {
        fun fromProto(proto:WorkbookKeyProto):WorkbookKey{
            return proto.toModel()
        }
    }
    fun pathScript():String{
        if(path!=null){
            return "\"${ path.toAbsolutePath().toString() }\""
        }else{
            return "None"
        }
    }
    fun toProto(): DocProtos.WorkbookKeyProto{
        val builder= DocProtos.WorkbookKeyProto.newBuilder().setName(name)
        if(this.path!=null){
            builder.setPath(this.path.toAbsolutePath().toString())
        }
        return builder.build()
    }
}

fun DocProtos.WorkbookKeyProto.toModel(): WorkbookKey {
    return WorkbookKey(
        name = name,
        path = if (this.hasPath()) {
            Path.of(path)
        } else {
            null
        }
    )
}
