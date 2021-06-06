package io.github.wykopmobilny.patrons.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PatronBadge(
    @JsonProperty("color") val hexColor: String,
    @JsonProperty("text") val text: String
)
