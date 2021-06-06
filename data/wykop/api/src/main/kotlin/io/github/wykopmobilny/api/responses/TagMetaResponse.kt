package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TagMetaResponse(
    @JsonProperty("tag") val tag: String,
    @JsonProperty("is_observed") var isObserved: Boolean,
    @JsonProperty("is_blocked") var isBlocked: Boolean,
    @JsonProperty("is_own") val isOwn: Boolean,
    @JsonProperty("description") val description: String?,
    @JsonProperty("background") val background: String?
)
