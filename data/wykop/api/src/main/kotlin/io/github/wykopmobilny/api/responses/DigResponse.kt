package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DigResponse(
    @field:Json(name = "digs") val diggs: Int,
    @field:Json(name = "buries") val buries: Int
)
