package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnswerResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "answer") val answer: String,
    @field:Json(name = "count") val count: Int,
    @field:Json(name = "percentage") val percentage: Double
)
