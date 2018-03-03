package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed

import com.squareup.moshi.Json

data class StreamableFile(
        @Json(name="url")
        val url : String
)