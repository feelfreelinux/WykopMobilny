package io.github.feelfreelinux.wykopmobilny.api.entries

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.DeleteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.EntryResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.VoteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.entries.FavoriteEntryResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
data class AddResponse(val id : Int)
interface EntriesRetrofitApi {
    @GET("/entries/index/{entryId}/appkey/$APP_KEY")
    fun getEntryIndex(@Path("entryId") entryId : Int) : Single<EntryResponse>

    @GET("/entries/vote/entry/{entryId}/appkey/$APP_KEY")
    fun voteEntry(@Path("entryId") entryId: Int) : Single<VoteResponse>

    @GET("/entries/unvote/entry/{entryId}/appkey/$APP_KEY")
    fun unvoteEntry(@Path("entryId") entryId: Int) : Single<VoteResponse>

    @GET("/entries/vote/comment/{entryId}/{commentId}/appkey/$APP_KEY")
    fun voteComment(@Path("entryId") entryId: Int, @Path("commentId") commentId: Int) : Single<VoteResponse>

    @GET("/entries/unvote/comment/{entryId}/{commentId}/appkey/$APP_KEY")
    fun unvoteComment(@Path("entryId") entryId: Int, @Path("commentId") commentId: Int) : Single<VoteResponse>

    @GET("/entries/favorite/{entryId}/appkey/$APP_KEY")
    fun markFavorite(@Path("entryId") entryId: Int) : Single<FavoriteEntryResponse>

    @Multipart
    @POST("/entries/add/appkey/$APP_KEY")
    fun addEntry(@Part("body") body: RequestBody,
                 @Part file : MultipartBody.Part) : Single<AddResponse>

    @FormUrlEncoded
    @POST("/entries/add/appkey/$APP_KEY")
    fun addEntry(@Field("body") body: String,
                 @Field("embed") embed : String?) : Single<AddResponse>

    @Multipart
    @POST("/entries/addcomment/{entryId}/appkey/$APP_KEY")
    fun addEntryComment(@Part("body") body: RequestBody,
                        @Path("entryId") entryId : Int,
                        @Part file : MultipartBody.Part) : Single<AddResponse>

    @FormUrlEncoded
    @POST("/entries/addcomment/{entryId}/appkey/$APP_KEY")
    fun addEntryComment(@Field("body") body: String,
                        @Field("embed") embed : String?,
                        @Path("entryId") entryId : Int) : Single<AddResponse>

    @FormUrlEncoded
    @POST("/entries/edit/{entryId}/appkey/$APP_KEY")
    fun editEntry(@Field("body") body: String,
                        @Path("entryId") entryId : Int) : Single<AddResponse>

    @GET("/entries/delete/{entryId}/appkey/$APP_KEY")
    fun deleteEntry(@Path("entryId") entryId: Int) : Single<DeleteResponse>
}