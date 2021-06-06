package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class UpvoterResponse(
    @JsonProperty("author") val author: AuthorResponse,
    @JsonProperty("date") val date: String
)
