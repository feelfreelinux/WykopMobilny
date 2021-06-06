package io.github.wykopmobilny.api.endpoints

import io.github.wykopmobilny.APP_KEY
import io.github.wykopmobilny.api.responses.BadgeResponse
import io.github.wykopmobilny.api.responses.EntryCommentResponse
import io.github.wykopmobilny.api.responses.EntryLinkResponse
import io.github.wykopmobilny.api.responses.EntryResponse
import io.github.wykopmobilny.api.responses.LinkCommentResponse
import io.github.wykopmobilny.api.responses.LinkResponse
import io.github.wykopmobilny.api.responses.ObserveStateResponse
import io.github.wykopmobilny.api.responses.ProfileResponse
import io.github.wykopmobilny.api.responses.RelatedResponse
import io.github.wykopmobilny.api.responses.WykopApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileRetrofitApi {
    @GET("/profiles/index/{username}/appkey/$APP_KEY")
    suspend fun getIndex(@Path("username") username: String): WykopApiResponse<ProfileResponse>

    // APIV2 WTF PAGINATION IS BROKEN HERE
    @GET("/profiles/actions/{username}/appkey/$APP_KEY/page/1")
    suspend fun getActions(@Path("username") username: String): WykopApiResponse<List<EntryLinkResponse>>

    @GET("/profiles/added/{username}/page/{page}/appkey/$APP_KEY")
    suspend fun getAdded(@Path("username") username: String, @Path("page") page: Int): WykopApiResponse<List<LinkResponse>>

    @GET("/profiles/published/{username}/page/{page}/appkey/$APP_KEY")
    suspend fun getPublished(@Path("username") username: String, @Path("page") page: Int): WykopApiResponse<List<LinkResponse>>

    @GET("/profiles/digged/{username}/page/{page}/appkey/$APP_KEY")
    suspend fun getDigged(@Path("username") username: String, @Path("page") page: Int): WykopApiResponse<List<LinkResponse>>

    @GET("/profiles/buried/{username}/page/{page}/appkey/$APP_KEY")
    suspend fun getBuried(@Path("username") username: String, @Path("page") page: Int): WykopApiResponse<List<LinkResponse>>

    @GET("/profiles/comments/{username}/page/{page}/appkey/$APP_KEY")
    suspend fun getLinkComments(@Path("username") username: String, @Path("page") page: Int): WykopApiResponse<List<LinkCommentResponse>>

    @GET("/profiles/entries/{username}/page/{page}/appkey/$APP_KEY")
    suspend fun getEntries(@Path("username") username: String, @Path("page") page: Int): WykopApiResponse<List<EntryResponse>>

    @GET("/profiles/entriescomments/{username}/page/{page}/appkey/$APP_KEY/data/full")
    suspend fun getEntriesComments(
        @Path("username") username: String,
        @Path("page") page: Int
    ): WykopApiResponse<List<EntryCommentResponse>>

    @GET("/profiles/related/{username}/page/{page}/appkey/$APP_KEY")
    suspend fun getRelated(@Path("username") username: String, @Path("page") page: Int): WykopApiResponse<List<RelatedResponse>>

    @GET("/profiles/observe/{username}/appkey/$APP_KEY")
    suspend fun observe(@Path("username") username: String): WykopApiResponse<ObserveStateResponse>

    @GET("/profiles/unobserve/{username}/appkey/$APP_KEY")
    suspend fun unobserve(@Path("username") username: String): WykopApiResponse<ObserveStateResponse>

    @GET("/profiles/block/{username}/appkey/$APP_KEY")
    suspend fun block(@Path("username") username: String): WykopApiResponse<ObserveStateResponse>

    @GET("/profiles/unblock/{username}/appkey/$APP_KEY")
    suspend fun unblock(@Path("username") username: String): WykopApiResponse<ObserveStateResponse>

    @GET("/profiles/badges/{username}/page/{page}/appkey/$APP_KEY/data/full")
    suspend fun getBadges(@Path("username") username: String, @Path("page") page: Int): WykopApiResponse<List<BadgeResponse>>
}
