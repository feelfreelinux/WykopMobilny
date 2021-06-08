package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Gfycat(
    @field:Json(name = "gfyItem") val gfyItem: GfycatItem
)
