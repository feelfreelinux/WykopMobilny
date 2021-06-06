package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class RelatedResponse(
    @JsonProperty("id") val id: Int,
    @JsonProperty("url") val url: String,
    @JsonProperty("vote_count") val voteCount: Int,
    @JsonProperty("author") val author: AuthorResponse?,
    @JsonProperty("title") val title: String,
    @JsonProperty("user_vote") val userVote: Int?
)
