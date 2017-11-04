package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2

import com.squareup.moshi.Json

data class WykopApiResponse<out T : Any>(
        @Json(name = "data")
        val data : T?,

        @Json(name = "error")
        val error : WykopErrorResponse?
)