package io.github.wykopmobilny.patrons.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PatronBadge(
    @field:Json(name = "color") val hexColor: String,
    @field:Json(name = "text") val text: String,
)
