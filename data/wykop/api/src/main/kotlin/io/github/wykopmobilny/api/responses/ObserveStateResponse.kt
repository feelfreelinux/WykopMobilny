package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ObserveStateResponse(
    @field:Json(name = "is_observed") val isObserved: Boolean,
    @field:Json(name = "is_blocked") val isBlocked: Boolean
)
