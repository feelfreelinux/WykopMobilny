package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TagSuggestionResponse(
    @JsonProperty("tag") val tag: String,
    @JsonProperty("followers") val followers: Int
)
