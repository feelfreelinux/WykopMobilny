package io.github.wykopmobilny.patrons.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Patron(
    @get:Json(name = "listMention") val listMention: Boolean,
    @get:Json(name = "tier") val tier: String,
    @get:Json(name = "username") val username: String,
    @get:Json(name = "badge") val badge: PatronBadge,
)
