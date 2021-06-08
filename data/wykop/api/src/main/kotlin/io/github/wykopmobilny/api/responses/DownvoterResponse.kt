package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DownvoterResponse(
    @field:Json(name = "author") val author: AuthorResponse,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "reason") val reason: Int
)
