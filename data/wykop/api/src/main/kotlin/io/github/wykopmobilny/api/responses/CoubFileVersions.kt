package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoubFileVersions(
    @field:Json(name = "mobile") val mobile: CoubMobileVersions
)
