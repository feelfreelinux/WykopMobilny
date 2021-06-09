package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BanResponse(
    @field:Json(name = "reason") val reason: String?,
    @field:Json(name = "date") val date: String?
)
