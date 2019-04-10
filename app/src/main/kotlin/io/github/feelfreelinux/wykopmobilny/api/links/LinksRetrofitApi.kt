package io.github.feelfreelinux.wykopmobilny.api.links

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DownvoterResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkCommentResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkVoteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.RelatedResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.UpvoterResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.VoteResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface LinksRetrofitApi {

    @GET("/links/upcoming/page/{page}/sort/{sortBy}/appkey/$APP_KEY")
    fun getUpcoming(
        @Path("page") page: Int,
        @Path("sortBy") sortBy: String
    ): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/links/promoted/page/{page}/appkey/$APP_KEY")
    fun getPromoted(@Path("page") page: Int): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/links/observed/page/{page}/appkey/$APP_KEY")
    fun getObserved(@Path("page") page: Int): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/links/comments/{linkId}/sort/{sortBy}/appkey/$APP_KEY")
    fun getLinkComments(@Path("linkId") linkId: Int, @Path("sortBy") sortBy: String): Single<WykopApiResponse<List<LinkCommentResponse>>>

    @GET("/links/link/{linkId}/appkey/$APP_KEY")
    fun getLink(@Path("linkId") linkId: Int): Single<WykopApiResponse<LinkResponse>>

    @GET("/links/commentVoteUp/1/{linkId}/appkey/$APP_KEY")
    fun commentVoteUp(@Path("linkId") linkId: Int): Single<WykopApiResponse<LinkVoteResponse>>


    @GET("/links/commentVoteDown/1/{linkId}/appkey/$APP_KEY")
    fun commentVoteDown(@Path("linkId") linkId: Int): Single<WykopApiResponse<LinkVoteResponse>>


    @GET("/links/commentVoteCancel/1/{linkId}/appkey/$APP_KEY")
    fun commentVoteCancel(@Path("linkId") linkId: Int): Single<WykopApiResponse<LinkVoteResponse>>

    @GET("/links/upvoters/{linkId}/appkey/$APP_KEY")
    fun getUpvoters(@Path("linkId") linkId: Int): Single<WykopApiResponse<List<UpvoterResponse>>>

    @GET("/links/downvoters/{linkId}/appkey/$APP_KEY")
    fun getDownvoters(@Path("linkId") linkId: Int): Single<WykopApiResponse<List<DownvoterResponse>>>

    @GET("/links/related/{linkId}/appkey/$APP_KEY")
    fun getRelated(@Path("linkId") linkId: Int): Single<WykopApiResponse<List<RelatedResponse>>>

    @GET("/links/voteup/{linkId}/appkey/$APP_KEY")
    fun voteUp(@Path("linkId") linkId: Int): Single<WykopApiResponse<DigResponse>>

    @GET("/links/votedown/{linkId}/{voteType}/appkey/$APP_KEY")
    fun voteDown(
        @Path("linkId") linkId: Int,
        @Path("voteType") reason: Int
    ): Single<WykopApiResponse<DigResponse>>

    @GET("/links/relatedvoteup/0/{relatedId}/appkey/$APP_KEY")
    fun relatedVoteUp(@Path("relatedId") relatedId: Int): Single<WykopApiResponse<VoteResponse>>

    @GET("/links/relatedvotedown/1/{relatedId}/appkey/$APP_KEY")
    fun relatedVoteDown(@Path("relatedId") relatedId: Int): Single<WykopApiResponse<VoteResponse>>

    @GET("/links/voteremove/{linkId}/appkey/$APP_KEY")
    fun voteRemove(@Path("linkId") linkId: Int): Single<WykopApiResponse<DigResponse>>

    @Multipart
    @POST("/links/commentadd/{linkId}/{commentId}/appkey/$APP_KEY")
    fun addComment(
        @Part("body") body: RequestBody,
        @Part("adultmedia") plus18: RequestBody,
        @Path("linkId") linkId: Int,
        @Path("commentId") commentId: Int,
        @Part file: MultipartBody.Part
    ): Single<WykopApiResponse<LinkCommentResponse>>

    @FormUrlEncoded
    @POST("/links/commentadd/{linkId}/{commentId}/appkey/$APP_KEY")
    fun addComment(
        @Field("body") body: String,
        @Path("linkId") linkId: Int,
        @Path("commentId") commentId: Int,
        @Field("embed") embed: String?,
        @Field("adultmedia") plus18: Boolean
    ): Single<WykopApiResponse<LinkCommentResponse>>

    @POST("/links/commentdelete/{linkCommentId}/appkey/$APP_KEY")
    fun deleteComment(@Path("linkCommentId") linkId: Int): Single<WykopApiResponse<LinkCommentResponse>>

    @Multipart
    @POST("/links/commentadd/{linkId}/appkey/$APP_KEY")
    fun addComment(
        @Part("body") body: RequestBody,
        @Part("adultmedia") plus18: RequestBody,
        @Path("linkId") linkId: Int,
        @Part file: MultipartBody.Part
    ): Single<WykopApiResponse<LinkCommentResponse>>

    @FormUrlEncoded
    @POST("/links/commentadd/{linkId}/appkey/$APP_KEY")
    fun addComment(
        @Field("body") body: String,
        @Path("linkId") linkId: Int,
        @Field("embed") embed: String?,
        @Field("adultmedia") plus18: Boolean
    ): Single<WykopApiResponse<LinkCommentResponse>>

    @FormUrlEncoded
    @POST("/links/relatedadd/{linkId}/appkey/$APP_KEY")
    fun addRelated(
            @Field("title") body: String,
            @Path("linkId") linkId: Int,
            @Field("url") url: String,
            @Field("plus18") plus18: Boolean
    ): Single<WykopApiResponse<RelatedResponse>>

    @FormUrlEncoded
    @POST("/links/commentedit/{linkId}/appkey/$APP_KEY")
    fun editComment(
        @Field("body") body: String,
        @Path("linkId") linkId: Int
    ): Single<WykopApiResponse<LinkCommentResponse>>

    @GET("/links/favorite/{linkId}/appkey/$APP_KEY")
    fun markFavorite(@Path("linkId") entryId: Int): Single<WykopApiResponse<List<Boolean>>>
}