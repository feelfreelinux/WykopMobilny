package io.github.wykopmobilny.api.endpoints

import io.github.wykopmobilny.APP_KEY
import io.github.wykopmobilny.api.responses.AuthorResponse
import io.github.wykopmobilny.api.responses.TagSuggestionResponse
import io.github.wykopmobilny.api.responses.WykopApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SuggestRetrofitApi {

    @GET("/suggest/tags/{suggestString}/appkey/$APP_KEY")
    suspend fun getTagSuggestions(@Path("suggestString") suggestString: String): WykopApiResponse<List<TagSuggestionResponse>>

    @GET("/suggest/users/{suggestString}/appkey/$APP_KEY")
    suspend fun getUsersSuggestions(@Path("suggestString") suggestString: String): WykopApiResponse<List<AuthorResponse>>
}
