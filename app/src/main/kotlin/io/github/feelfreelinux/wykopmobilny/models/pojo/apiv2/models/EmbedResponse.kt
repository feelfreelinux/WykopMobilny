package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class EmbedResponse(
        @Json(name="type")
        val type : String,

        @Json(name="url")
        val url : String,

        @Json(name="source")
        val source : String?,

        @Json(name="preview")
        val preview : String,

        @Json(name="plus18")
        val plus18 : Boolean,

        @Json(name="size")
        val size : String,

        @Json(name="animated")
        val animated : Boolean
)