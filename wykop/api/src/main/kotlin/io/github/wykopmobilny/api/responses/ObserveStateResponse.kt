package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class ObserveStateResponse(
    @JsonProperty("is_observed") val isObserved: Boolean,
    @JsonProperty("is_blocked") val isBlocked: Boolean
)
