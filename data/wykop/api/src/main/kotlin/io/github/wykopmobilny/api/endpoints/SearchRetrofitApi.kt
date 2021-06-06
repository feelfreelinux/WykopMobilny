package io.github.wykopmobilny.api.endpoints

import io.github.wykopmobilny.APP_KEY
import io.github.wykopmobilny.api.responses.WykopApiResponse
import io.github.wykopmobilny.api.responses.AuthorResponse
import io.github.wykopmobilny.api.responses.EntryResponse
import io.github.wykopmobilny.api.responses.LinkResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface SearchRetrofitApi {
    @FormUrlEncoded
    @POST("/search/links/page/{page}/appkey/$APP_KEY")
    suspend fun searchLinks(
        @Path("page") page: Int,
        @Field("q") query: String
    ): WykopApiResponse<List<LinkResponse>>

    @FormUrlEncoded
    @POST("/search/entries/page/{page}/appkey/$APP_KEY")
    suspend fun searchEntries(
        @Path("page") page: Int,
        @Field("q") query: String
    ): WykopApiResponse<List<EntryResponse>>

    @FormUrlEncoded
    @POST("/search/profiles/appkey/$APP_KEY")
    suspend fun searchProfiles(@Field("q") query: String): WykopApiResponse<List<AuthorResponse>>
}
