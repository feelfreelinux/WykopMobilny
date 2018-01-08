package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class LoginResponse(
        @Json(name="profile")
        val profile : ProfileResponse,

        @Json(name="userkey")
        val userkey : String
)