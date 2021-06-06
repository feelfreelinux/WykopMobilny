package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class LinkVoteResponse(
    @JsonProperty("vote_count_plus") val voteCountPlus: Int,
    @JsonProperty("vote_count") val voteCount: Int
) {
    val voteCountMinus: Int get() = voteCount - voteCountPlus
}
