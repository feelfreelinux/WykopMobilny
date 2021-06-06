package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class TagLinksResponse(
    @JsonProperty("data") override val data: List<LinkResponse>?,
    @JsonProperty("error") override val error: WykopErrorResponse?,
    @JsonProperty("meta") val meta: TagMetaResponse
) : ApiResponse<List<LinkResponse>>
