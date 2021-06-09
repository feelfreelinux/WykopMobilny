package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StreamableFiles(
    @field:Json(name = "mp4") val mp4: StreamableFile,
    @field:Json(name = "mp4-mobile") val mp4Mobile: StreamableFile?
)
