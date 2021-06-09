package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoubMobileVersions(
    @field:Json(name = "video") val mp4: String?,
    @field:Json(name = "audio") val audio: List<String>
)
