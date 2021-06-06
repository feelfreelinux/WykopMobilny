package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthorResponse(
    @JsonProperty("login") val login: String,
    @JsonProperty("color") val color: Int,
    @JsonProperty("sex") val sex: String?,
    @JsonProperty("avatar") val avatar: String
)
