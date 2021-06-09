package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GfycatItem(
    @field:Json(name = "mp4Url") val mp4Url: String,
    @field:Json(name = "gifUrl") val gifUrl: String,
    @field:Json(name = "webmUrl") val webmUrl: String
)
