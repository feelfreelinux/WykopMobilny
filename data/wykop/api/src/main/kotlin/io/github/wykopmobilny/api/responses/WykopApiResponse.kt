package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
open class WykopApiResponse<out T : Any>(
    @JsonProperty("data") override val data: T?,
    @JsonProperty("error") override val error: WykopErrorResponse?
) : ApiResponse<T>
