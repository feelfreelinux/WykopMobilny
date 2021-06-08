package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WykopErrorResponse(
    @field:Json(name = "code") val code: Int,
    @field:Json(name = "message_pl") val messagePl: String
)
