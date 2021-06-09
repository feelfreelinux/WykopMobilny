package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coub(
    @field:Json(name = "file_versions") val fileVersions: CoubFileVersions
)
