package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class FullConversationResponse(
    @JsonProperty("data") override val data: List<PMMessageResponse>?,
    @JsonProperty("error") override val error: WykopErrorResponse?,
    @JsonProperty("receiver") val receiver: AuthorResponse
) : ApiResponse<List<PMMessageResponse>>
