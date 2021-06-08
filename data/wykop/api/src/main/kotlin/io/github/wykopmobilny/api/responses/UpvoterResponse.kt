package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpvoterResponse(
    @field:Json(name = "author") val author: AuthorResponse,
    @field:Json(name = "date") val date: String
)
