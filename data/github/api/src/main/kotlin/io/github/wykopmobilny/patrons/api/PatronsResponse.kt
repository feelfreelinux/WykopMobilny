package io.github.wykopmobilny.patrons.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PatronsResponse(
    @field:Json(name = "patrons") val patrons: List<Patron>,
)
