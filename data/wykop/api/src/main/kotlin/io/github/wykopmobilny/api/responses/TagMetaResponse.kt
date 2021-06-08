package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagMetaResponse(
    @field:Json(name = "tag") val tag: String,
    @field:Json(name = "is_observed") var isObserved: Boolean,
    @field:Json(name = "is_blocked") var isBlocked: Boolean,
    @field:Json(name = "is_own") val isOwn: Boolean,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "background") val background: String?
)
