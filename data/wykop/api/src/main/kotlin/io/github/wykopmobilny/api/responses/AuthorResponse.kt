package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthorResponse(
    @field:Json(name = "login") val login: String,
    @field:Json(name = "color") val color: Int,
    @field:Json(name = "sex") val sex: String?,
    @field:Json(name = "avatar") val avatar: String
)
