package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class TagEntriesResponse(
    @JsonProperty("data") override val data: List<EntryResponse>?,
    @JsonProperty("error") override val error: WykopErrorResponse?,
    @JsonProperty("meta") val meta: TagMetaResponse
) : ApiResponse<List<EntryResponse>>
