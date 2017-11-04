package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common

import com.squareup.moshi.Json

open class WykopApiResponse<out T : Any> (
        @Json(name = "data")
        override val data : T?,

        @Json(name = "error")
        override val error : WykopErrorResponse?
) : ApiResponse<T>