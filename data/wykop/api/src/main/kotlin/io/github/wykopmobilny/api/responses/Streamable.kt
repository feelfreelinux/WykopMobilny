package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Streamable(
    @field:Json(name = "files") val files: StreamableFiles
)
