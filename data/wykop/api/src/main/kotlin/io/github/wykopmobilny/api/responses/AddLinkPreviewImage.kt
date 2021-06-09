package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddLinkPreviewImage(
    @field:Json(name = "key") val key: String,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "preview_url") val previewUrl: String,
    @field:Json(name = "source_url") val sourceUrl: String
)
