package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationResponse(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "author") val author: AuthorResponse?,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "body") val body: String,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "item_id") val itemId: String?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "new") val new: Boolean
)
