package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class ConversationDeleteResponse(
        @Json(name = "status")
        val status : String
)