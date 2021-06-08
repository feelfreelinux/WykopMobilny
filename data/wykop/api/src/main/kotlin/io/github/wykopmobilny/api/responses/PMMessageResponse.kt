package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PMMessageResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "body") val body: String?,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "direction") val direction: String,
    @field:Json(name = "embed") val embed: EmbedResponse?,
    @field:Json(name = "app") val app: String?
)
