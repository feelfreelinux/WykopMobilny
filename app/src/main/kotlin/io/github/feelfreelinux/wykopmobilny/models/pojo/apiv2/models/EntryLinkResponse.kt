package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class EntryLinkResponse(
        @Json(name="entry")
        val entry : EntryResponse,
        @Json(name="link")
        val link : LinkResponse
)