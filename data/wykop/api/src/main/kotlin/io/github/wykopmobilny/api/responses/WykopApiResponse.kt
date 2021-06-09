package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WykopApiResponse<out T : Any>(
    @field:Json(name = "data") override val data: T?,
    @field:Json(name = "error") override val error: WykopErrorResponse?
) : ApiResponse<T>
