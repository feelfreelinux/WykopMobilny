package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class ConversationResponse(
        @Json(name="last_update")
        val lastUpdate : String,

        @Json(name="receiver")
        val receiver : AuthorResponse,

        @Json(name="status")
        val status : String
)