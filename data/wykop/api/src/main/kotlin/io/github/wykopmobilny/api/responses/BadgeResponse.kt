package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BadgeResponse(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "icon") val icon: String
)
