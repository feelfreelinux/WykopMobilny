package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConversationResponse(
    @field:Json(name = "last_update") val lastUpdate: String,
    @field:Json(name = "receiver") val receiver: AuthorResponse,
    @field:Json(name = "status") val status: String
)
