package io.github.feelfreelinux.wykopmobilny.api.profile

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryCommentResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryLinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkCommentResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObserveStateResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ProfileResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.RelatedResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.BadgeResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileRetrofitApi {
    @GET("/profiles/index/{username}/appkey/$APP_KEY")
    fun getIndex(@Path("username") username: String): Single<WykopApiResponse<ProfileResponse>>

    // APIV2 WTF PAGINATION IS BROKEN HERE
    @GET("/profiles/actions/{username}/appkey/$APP_KEY/page/1")
    fun getActions(@Path("username") username: String): Single<WykopApiResponse<List<EntryLinkResponse>>>

    @GET("/profiles/added/{username}/page/{page}/appkey/$APP_KEY")
    fun getAdded(@Path("username") username: String, @Path("page") page: Int): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/profiles/published/{username}/page/{page}/appkey/$APP_KEY")
    fun getPublished(@Path("username") username: String, @Path("page") page: Int): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/profiles/digged/{username}/page/{page}/appkey/$APP_KEY")
    fun getDigged(@Path("username") username: String, @Path("page") page: Int): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/profiles/buried/{username}/page/{page}/appkey/$APP_KEY")
    fun getBuried(@Path("username") username: String, @Path("page") page: Int): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/profiles/comments/{username}/page/{page}/appkey/$APP_KEY")
    fun getLinkComments(@Path("username") username: String, @Path("page") page: Int): Single<WykopApiResponse<List<LinkCommentResponse>>>

    @GET("/profiles/entries/{username}/page/{page}/appkey/$APP_KEY")
    fun getEntries(@Path("username") username: String, @Path("page") page: Int): Single<WykopApiResponse<List<EntryResponse>>>

    @GET("/profiles/entriescomments/{username}/page/{page}/appkey/$APP_KEY/data/full")
    fun getEntriesComments(
        @Path("username") username: String,
        @Path("page") page: Int
    ): Single<WykopApiResponse<List<EntryCommentResponse>>>

    @GET("/profiles/related/{username}/page/{page}/appkey/$APP_KEY")
    fun getRelated(@Path("username") username: String, @Path("page") page: Int): Single<WykopApiResponse<List<RelatedResponse>>>

    @GET("/profiles/observe/{username}/appkey/$APP_KEY")
    fun observe(@Path("username") username: String): Single<WykopApiResponse<ObserveStateResponse>>

    @GET("/profiles/unobserve/{username}/appkey/$APP_KEY")
    fun unobserve(@Path("username") username: String): Single<WykopApiResponse<ObserveStateResponse>>

    @GET("/profiles/block/{username}/appkey/$APP_KEY")
    fun block(@Path("username") username: String): Single<WykopApiResponse<ObserveStateResponse>>

    @GET("/profiles/unblock/{username}/appkey/$APP_KEY")
    fun unblock(@Path("username") username: String): Single<WykopApiResponse<ObserveStateResponse>>

    @GET("/profiles/badges/{username}/page/{page}/appkey/$APP_KEY/data/full")
    fun getBadges(@Path("username") username: String, @Path("page") page: Int): Single<WykopApiResponse<List<BadgeResponse>>>
}
