package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BanResponse(
    @JsonProperty("reason") val reason: String?,
    @JsonProperty("date") val date: String?
)
