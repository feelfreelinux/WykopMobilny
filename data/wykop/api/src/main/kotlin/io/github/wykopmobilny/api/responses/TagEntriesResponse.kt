package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TagEntriesResponse(
    @field:Json(name = "data") override val data: List<EntryResponse>?,
    @field:Json(name = "error") override val error: WykopErrorResponse?,
    @field:Json(name = "meta") val meta: TagMetaResponse
) : ApiResponse<List<EntryResponse>>
