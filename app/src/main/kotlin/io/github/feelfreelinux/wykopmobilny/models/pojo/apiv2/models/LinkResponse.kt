package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class LinkResponse(
    @JsonProperty("id") val id: Int,
    @JsonProperty("title") val title: String?,
    @JsonProperty("description") val description: String?,
    @JsonProperty("tags") val tags: String,
    @JsonProperty("source_url") val sourceUrl: String,
    @JsonProperty("vote_count") val voteCount: Int,
    @JsonProperty("bury_count") val buryCount: Int,
    @JsonProperty("comments_count") val commentsCount: Int,
    @JsonProperty("related_count") val relatedCount: Int,
    @JsonProperty("author") val author: AuthorResponse?,
    @JsonProperty("date") val date: String,
    @JsonProperty("preview") val preview: String?,
    @JsonProperty("plus18") val plus18: Boolean,
    @JsonProperty("can_vote") val canVote: Boolean,
    @JsonProperty("is_hot") val isHot: Boolean,
    @JsonProperty("status") val status: String,
    @JsonProperty("user_vote") val userVote: String?,
    @JsonProperty("user_favorite") val userFavorite: Boolean?,
    @JsonProperty("app") val app: String?
)