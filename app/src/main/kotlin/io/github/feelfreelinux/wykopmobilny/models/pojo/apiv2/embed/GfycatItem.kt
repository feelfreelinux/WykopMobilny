package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed

import com.squareup.moshi.Json

data class GfycatItem(
        @Json(name="mp4Url")
        val mp4Url : String,
        @Json(name="gifUrl")
        val gifUrl : String,
        @Json(name="webmUrl")
        val webmUrl : String
)
