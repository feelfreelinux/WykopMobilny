package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FullConversationResponse(
    @field:Json(name = "data") override val data: List<PMMessageResponse>?,
    @field:Json(name = "error") override val error: WykopErrorResponse?,
    @field:Json(name = "receiver") val receiver: AuthorResponse
) : ApiResponse<List<PMMessageResponse>>
