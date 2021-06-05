package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class EntryCommentResponse(
    @JsonProperty("id") val id: Int,
    @JsonProperty("entry_id") val entryId: Int?,
    @JsonProperty("author") val author: AuthorResponse,
    @JsonProperty("date") val date: String,
    @JsonProperty("body") val body: String?,
    @JsonProperty("blocked") val blocked: Boolean,
    @JsonProperty("favorite") val favorite: Boolean,
    @JsonProperty("vote_count") val voteCount: Int,
    @JsonProperty("status") val status: String,
    @JsonProperty("user_vote") val userVote: Int,
    @JsonProperty("embed") val embed: EmbedResponse?,
    @JsonProperty("app") val app: String?,
    @JsonProperty("violation_url") val violationUrl: String?
)
