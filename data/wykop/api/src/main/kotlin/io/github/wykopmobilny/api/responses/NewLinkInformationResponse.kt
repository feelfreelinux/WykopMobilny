package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewLinkInformationResponse(
    @field:Json(name = "key") val key: String,
    @field:Json(name = "title") val title: String? = "",
    @field:Json(name = "description") val description: String? = "",
    @field:Json(name = "source_url") val sourceUrl: String
)
