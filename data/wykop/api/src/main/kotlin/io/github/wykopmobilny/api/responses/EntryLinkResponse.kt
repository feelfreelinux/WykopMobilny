package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EntryLinkResponse(
    @field:Json(name = "entry") val entry: EntryResponse?,
    @field:Json(name = "link") val link: LinkResponse?
)
