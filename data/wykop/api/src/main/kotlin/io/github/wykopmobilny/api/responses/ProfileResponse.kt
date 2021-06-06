package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProfileResponse(
    @JsonProperty("login") val login: String,
    @JsonProperty("color") val color: Int,
    @JsonProperty("sex") val sex: String?,
    @JsonProperty("avatar") val avatar: String,
    @JsonProperty("signup_at") val signupAt: String,
    @JsonProperty("is_blocked") val isBlocked: Boolean? = false,
    @JsonProperty("is_observed") val isObserved: Boolean? = false,
    @JsonProperty("background") val background: String?,
    @JsonProperty("links_added_count") val linksAddedCount: Int,
    @JsonProperty("links_published_count") val linksPublishedCount: Int,
    @JsonProperty("rank") val rank: Int?,
    @JsonProperty("followers") val followers: Int,
    @JsonProperty("about") val description: String?,
    @JsonProperty("violation_url") val violationUrl: String?,
    @JsonProperty("ban") val ban: BanResponse?
)
