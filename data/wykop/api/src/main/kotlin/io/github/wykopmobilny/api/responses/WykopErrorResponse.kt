package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class WykopErrorResponse(
    @JsonProperty("code") val code: Int,
    @JsonProperty("message_pl") val messagePl: String
)
