package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagSuggestionResponse(
    @field:Json(name = "tag") val tag: String,
    @field:Json(name = "followers") val followers: Int
)
