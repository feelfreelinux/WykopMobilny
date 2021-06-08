package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewLinkResponse(
    @field:Json(name = "data") override val data: NewLinkInformationResponse?,
    @field:Json(name = "error") override val error: WykopErrorResponse?,
    @field:Json(name = "duplicates") val duplicates: List<LinkResponse>? = emptyList()
) : ApiResponse<NewLinkInformationResponse>
