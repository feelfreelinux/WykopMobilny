package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class UpvoterResponse(
        @Json(name = "author")
        val author : AuthorResponse,
        @Json(name = "date")
        val date : String
)