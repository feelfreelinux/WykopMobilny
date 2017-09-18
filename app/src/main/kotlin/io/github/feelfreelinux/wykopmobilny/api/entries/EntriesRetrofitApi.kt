package io.github.feelfreelinux.wykopmobilny.api.entries

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.api.VoteResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
data class AddResponse(val id : Int)
interface EntriesRetrofitApi {
    @GET("/entries/index/{entryId}/appkey/$APP_KEY/{userkey}")
    fun getEntryIndex(@Path("entryId") entryId : Int, @Path("userkey", encoded = true) userKey : String) : Call<Entry>

    // VOTE
    @GET("/entries/vote/entry/{entryId}/appkey/$APP_KEY/{userkey}")
    fun voteEntry(@Path("entryId") entryId: Int, @Path("userkey", encoded = true) userKey : String) : Call<VoteResponse>

    @GET("/entries/unvote/entry/{entryId}/appkey/$APP_KEY/{userkey}")
    fun unvoteEntry(@Path("entryId") entryId: Int, @Path("userkey", encoded = true) userKey : String) : Call<VoteResponse>

    @GET("/entries/vote/comment/{entryId}/{commentId}/appkey/$APP_KEY/{userkey}")
    fun voteComment(@Path("entryId") entryId: Int, @Path("commentId") commentId: Int, @Path("userkey", encoded = true) userKey : String) : Call<VoteResponse>

    @GET("/entries/unvote/comment/{entryId}/{commentId}/appkey/$APP_KEY/{userkey}")
    fun unvoteComment(@Path("entryId") entryId: Int, @Path("commentId") commentId: Int, @Path("userkey", encoded = true) userKey : String) : Call<VoteResponse>

    // ADD
    @Multipart
    @POST("/entries/add/appkey/$APP_KEY/{userkey}")
    fun addEntry(@Part("body") body: RequestBody,
                 @Path("userkey", encoded = true) userKey : String,
                 @Part file : MultipartBody.Part) : Call<AddResponse>

    // ADD
    @FormUrlEncoded
    @POST("/entries/add/appkey/$APP_KEY/{userkey}")
    fun addEntry(@Field("body") body: String,
                 @Field("embed") embed : String,
                 @Path("userkey", encoded = true) userKey : String) : Call<AddResponse>

    @Multipart
    @POST("/entries/addcomment/{entryId}/appkey/$APP_KEY/{userkey}")
    fun addEntryComment(@Part("body") body: RequestBody,
                        @Path("entryId") entryId : Int,
                        @Path("userkey", encoded = true) userKey : String,
                        @Part file : MultipartBody.Part) : Call<AddResponse>

    @FormUrlEncoded
    @POST("/entries/addcomment/{entryId}/appkey/$APP_KEY/{userkey}")
    fun addEntryComment(@Field("body") body: String,
                        @Field("embed") embed : String,
                        @Path("entryId") entryId : Int,
                        @Path("userkey", encoded = true) userKey : String) : Call<AddResponse>
}