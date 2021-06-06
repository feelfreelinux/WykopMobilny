package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class EntryResponse(
    @JsonProperty("id") val id: Int,
    @JsonProperty("date") val date: String,
    @JsonProperty("body") val body: String?,
    @JsonProperty("author") val author: AuthorResponse,
    @JsonProperty("blocked") val blocked: Boolean,
    @JsonProperty("favorite") val favorite: Boolean,
    @JsonProperty("vote_count") val voteCount: Int,
    @JsonProperty("comments_count") val commentsCount: Int,
    @JsonProperty("comments") val comments: List<EntryCommentResponse>?,
    @JsonProperty("status") val status: String,
    @JsonProperty("embed") val embed: EmbedResponse?,
    @JsonProperty("survey") val survey: SurveyResponse?,
    @JsonProperty("user_vote") val userVote: Int,
    @JsonProperty("violation_url") val violationUrl: String?,
    @JsonProperty("app") val app: String?,
    @JsonProperty("can_comment") val isCommentingPossible: Boolean
)
