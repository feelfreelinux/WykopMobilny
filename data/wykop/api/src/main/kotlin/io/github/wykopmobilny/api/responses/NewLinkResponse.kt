package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class NewLinkResponse(
    @JsonProperty("data") override val data: NewLinkInformationResponse?,
    @JsonProperty("error") override val error: WykopErrorResponse?,
    @JsonProperty("duplicates") val duplicates: List<LinkResponse>? = emptyList()
) : ApiResponse<NewLinkInformationResponse>
