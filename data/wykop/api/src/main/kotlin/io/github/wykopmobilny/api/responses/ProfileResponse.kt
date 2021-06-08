package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileResponse(
    @field:Json(name = "login") val login: String,
    @field:Json(name = "color") val color: Int,
    @field:Json(name = "sex") val sex: String?,
    @field:Json(name = "avatar") val avatar: String,
    @field:Json(name = "signup_at") val signupAt: String,
    @field:Json(name = "is_blocked") val isBlocked: Boolean? = false,
    @field:Json(name = "is_observed") val isObserved: Boolean? = false,
    @field:Json(name = "background") val background: String?,
    @field:Json(name = "links_added_count") val linksAddedCount: Int,
    @field:Json(name = "links_published_count") val linksPublishedCount: Int,
    @field:Json(name = "rank") val rank: Int?,
    @field:Json(name = "followers") val followers: Int,
    @field:Json(name = "about") val description: String?,
    @field:Json(name = "violation_url") val violationUrl: String?,
    @field:Json(name = "ban") val ban: BanResponse?
)
