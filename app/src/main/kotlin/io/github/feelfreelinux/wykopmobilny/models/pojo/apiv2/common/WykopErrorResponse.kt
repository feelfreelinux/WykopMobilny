package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common

import com.squareup.moshi.Json

class WykopErrorResponse(
        @Json(name = "code")
        val code : Int,

        @Json(name = "message_en")
        val messageEn : String,

        @Json(name = "message_pl")
        val messagePl : String
)