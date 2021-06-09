package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LinkResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "tags") val tags: String,
    @field:Json(name = "source_url") val sourceUrl: String,
    @field:Json(name = "vote_count") val voteCount: Int,
    @field:Json(name = "bury_count") val buryCount: Int,
    @field:Json(name = "comments_count") val commentsCount: Int,
    @field:Json(name = "related_count") val relatedCount: Int,
    @field:Json(name = "author") val author: AuthorResponse?,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "preview") val preview: String?,
    @field:Json(name = "plus18") val plus18: Boolean,
    @field:Json(name = "can_vote") val canVote: Boolean,
    @field:Json(name = "is_hot") val isHot: Boolean,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "user_vote") val userVote: String?,
    @field:Json(name = "user_favorite") val userFavorite: Boolean?,
    @field:Json(name = "app") val app: String?,
)
