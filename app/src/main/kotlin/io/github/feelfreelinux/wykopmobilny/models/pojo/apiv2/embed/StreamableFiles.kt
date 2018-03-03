package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed

import com.squareup.moshi.Json

data class StreamableFiles(
        @Json(name="mp4")
        val mp4 : StreamableFile,
        @Json(name="mp4-mobile")
        val mp4Mobile : StreamableFile
)
