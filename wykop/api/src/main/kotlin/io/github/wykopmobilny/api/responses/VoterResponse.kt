package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class VoterResponse(
    @JsonProperty("author") val author: AuthorResponse,
    @JsonProperty("date") val date: String,
    @JsonProperty("vote_type") val voteType: Int
)
