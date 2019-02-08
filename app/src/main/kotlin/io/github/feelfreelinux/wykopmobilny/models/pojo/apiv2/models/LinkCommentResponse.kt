package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LinkCommentResponse(
    @JsonProperty("id") val id: Int,
    @JsonProperty("author") val author: AuthorResponse?,
    @JsonProperty("date") val date: String,
    @JsonProperty("body") val body: String?,
    @JsonProperty("blocked") val blocked: Boolean,
    @JsonProperty("favorite") val favorite: Boolean,
    @JsonProperty("vote_count") val voteCount: Int,
    @JsonProperty("vote_count_plus") val voteCountPlus: Int,
    @JsonProperty("user_vote") val userVote: Int,
    @JsonProperty("parent_id") val parentId: Int,
    @JsonProperty("can_vote") val canVote: Boolean,
    @JsonProperty("link_id") val linkId: Int,
    @JsonProperty("embed") val embed: EmbedResponse?,
    @JsonProperty("violation_url") val violationUrl: String?,
    @JsonProperty("app") val app: String?
)