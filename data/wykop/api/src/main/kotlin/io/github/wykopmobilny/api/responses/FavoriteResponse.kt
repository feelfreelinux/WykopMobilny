package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteResponse(
    @field:Json(name = "user_favorite") val userFavorite: Boolean
)
