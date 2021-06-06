package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BadgeResponse(
    @JsonProperty("name") val name: String,
    @JsonProperty("date") val date: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("icon") val icon: String
)
