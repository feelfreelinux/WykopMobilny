package io.github.feelfreelinux.wykopmobilny.api.entries

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface EntriesRetrofitApi {
    @GET("/entries/hot/page/{page}/period/{period}/appkey/$APP_KEY")
    fun getHot(@Path("page") page : Int, @Path("period") period : String) : Single<WykopApiResponse<List<EntryResponse>>>

    @GET("/entries/stream/page/{page}/appkey/$APP_KEY")
    fun getStream(@Path("page") page : Int) : Single<WykopApiResponse<List<EntryResponse>>>

    @GET("/entries/entry/{id}/appkey/$APP_KEY")
    fun getEntry(@Path("id") id : Int) : Single<WykopApiResponse<EntryResponse>>

    @GET("/entries/voteup/{entryId}/appkey/$APP_KEY")
    fun voteEntry(@Path("entryId") entryId: Int) : Single<WykopApiResponse<VoteResponse>>

    @GET("/entries/voteremove/{entryId}/appkey/$APP_KEY")
    fun unvoteEntry(@Path("entryId") entryId: Int) : Single<WykopApiResponse<VoteResponse>>

    @GET("/entries/commentvoteup/{commentId}/appkey/$APP_KEY")
    fun voteComment(@Path("commentId") commentId: Int) : Single<WykopApiResponse<VoteResponse>>

    @GET("/entries/commentvoteremove/{commentId}/appkey/$APP_KEY")
    fun unvoteComment(@Path("commentId") commentId: Int) :  Single<WykopApiResponse<VoteResponse>>

    @GET("/entries/favorite/{entryId}/appkey/$APP_KEY")
    fun markFavorite(@Path("entryId") entryId: Int) : Single<WykopApiResponse<FavoriteResponse>>

    @GET("/entries/surveyvote/{entryId}/{answerId}/appkey/$APP_KEY")
    fun voteSurvey(@Path("entryId") entryId: Int, @Path("answerId") answerId : Int) : Single<WykopApiResponse<SurveyResponse>>

    @Multipart
    @POST("/entries/add/appkey/$APP_KEY")
    fun addEntry(@Part("body") body: RequestBody,
                 @Part("adultmedia") plus18 : RequestBody,
                 @Part file : MultipartBody.Part) : Single<WykopApiResponse<EntryResponse>>


    @GET("/entries/upvoters/{entryId}/appkey/$APP_KEY")
    fun getEntryVoters(@Path("entryId") entryId: Int) : Single<WykopApiResponse<List<VoterResponse>>>

    @GET("/entries/commentupvoters/{commentId}/appkey/$APP_KEY")
    fun getCommentUpvoters(@Path("commentId") commentId: Int) : Single<WykopApiResponse<List<VoterResponse>>>

    @FormUrlEncoded
    @POST("/entries/add/appkey/$APP_KEY")
    fun addEntry(@Field("body") body: String,
                 @Field("embed") embed : String?,
                 @Field("adultmedia") plus18 : Boolean) : Single<WykopApiResponse<EntryResponse>>

    @Multipart
    @POST("/entries/commentadd/{entryId}/appkey/$APP_KEY")
    fun addEntryComment(@Part("body") body: RequestBody,
                        @Part("adultmedia") plus18 : RequestBody,
                        @Path("entryId") entryId : Int,
                        @Part file : MultipartBody.Part) : Single<WykopApiResponse<EntryCommentResponse>>

    @FormUrlEncoded
    @POST("/entries/commentadd/{entryId}/appkey/$APP_KEY")
    fun addEntryComment(@Field("body") body: String,
                        @Field("embed") embed : String?,
                        @Field("adultmedia") plus18 : Boolean,
                        @Path("entryId") entryId : Int) : Single<WykopApiResponse<EntryCommentResponse>>

    @FormUrlEncoded
    @POST("/entries/edit/{entryId}/appkey/$APP_KEY")
    fun editEntry(@Field("body") body: String,
                        @Path("entryId") entryId : Int) : Single<WykopApiResponse<EntryResponse>>

    @GET("/entries/delete/{entryId}/appkey/$APP_KEY")
    fun deleteEntry(@Path("entryId") entryId: Int) : Single<WykopApiResponse<EntryResponse>>

    @FormUrlEncoded
    @POST("/entries/commentedit/{commentId}/appkey/$APP_KEY")
    fun editEntryComment(@Field("body") body: String,
                         @Path("commentId") commentId: Int) : Single<WykopApiResponse<EntryCommentResponse>>

    @GET("/entries/commentdelete/{commentId}/appkey/$APP_KEY")
    fun deleteEntryComment(@Path("commentId") commentId: Int) : Single<WykopApiResponse<EntryCommentResponse>>
}