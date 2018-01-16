package io.github.feelfreelinux.wykopmobilny.api.links

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface LinksRetrofitApi {
    @GET("/links/promoted/page/{page}/appkey/$APP_KEY")
    fun getPromoted(@Path("page") page : Int) : Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/links/comments/{linkId}/sort/{sortBy}/appkey/$APP_KEY")
    fun getLinkComments(@Path("linkId") linkId : Int, @Path("sortBy") sortBy : String) : Single<WykopApiResponse<List<LinkCommentResponse>>>

    @GET("/links/link/{linkId}/appkey/$APP_KEY")
    fun getLink(@Path("linkId") linkId : Int) : Single<WykopApiResponse<LinkResponse>>

    @GET("/links/commentVoteUp/1/{linkId}/appkey/$APP_KEY")
    fun commentVoteUp(@Path("linkId") linkId : Int) : Single<WykopApiResponse<LinkVoteResponse>>


    @GET("/links/commentVoteDown/1/{linkId}/appkey/$APP_KEY")
    fun commentVoteDown(@Path("linkId") linkId : Int) : Single<WykopApiResponse<LinkVoteResponse>>


    @GET("/links/commentVoteCancel/1/{linkId}/appkey/$APP_KEY")
    fun commentVoteCancel(@Path("linkId") linkId : Int) : Single<WykopApiResponse<LinkVoteResponse>>

    @GET("/links/voteup/{linkId}/appkey/$APP_KEY")
    fun voteUp(@Path("linkId") linkId : Int) : Single<WykopApiResponse<DigResponse>>

    @GET("/links/votedown/{linkId}/{voteType}/appkey/$APP_KEY")
    fun voteDown(@Path("linkId") linkId : Int,
                 @Path("voteType") reason : Int) : Single<WykopApiResponse<DigResponse>>

    @GET("/links/voteremove/{linkId}/appkey/$APP_KEY")
    fun voteRemove(@Path("linkId") linkId : Int) : Single<WykopApiResponse<DigResponse>>

    @Multipart
    @POST("/links/commentadd/{linkId}/{commentId}/appkey/$APP_KEY")
    fun addComment(@Part("body") body: RequestBody,
                        @Path("linkId") linkId : Int,
                        @Path("commentId") commentId : Int,
                        @Part file : MultipartBody.Part) : Single<WykopApiResponse<LinkCommentResponse>>

    @FormUrlEncoded
    @POST("/links/commentadd/{linkId}/{commentId}/appkey/$APP_KEY")
    fun addComment(@Field("body") body: String,
                   @Path("linkId") linkId : Int,
                   @Path("commentId") commentId : Int,
                   @Field("embed") embed : String?) : Single<WykopApiResponse<LinkCommentResponse>>

    @POST("/links/commentdelete/{linkCommentId}/appkey/$APP_KEY")
    fun deleteComment(@Path("linkCommentId") linkId : Int) : Single<WykopApiResponse<LinkCommentResponse>>

    @Multipart
    @POST("/links/commentadd/{linkId}/appkey/$APP_KEY")
    fun addComment(@Part("body") body: RequestBody,
                   @Path("linkId") linkId : Int,
                   @Part file : MultipartBody.Part) : Single<WykopApiResponse<LinkCommentResponse>>

    @FormUrlEncoded
    @POST("/links/commentadd/{linkId}/appkey/$APP_KEY")
    fun addComment(@Field("body") body: String,
                   @Path("linkId") linkId : Int,
                   @Field("embed") embed : String?) : Single<WykopApiResponse<LinkCommentResponse>>
}