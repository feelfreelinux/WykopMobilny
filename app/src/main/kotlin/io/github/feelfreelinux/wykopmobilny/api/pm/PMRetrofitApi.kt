package io.github.feelfreelinux.wykopmobilny.api.pm

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.api.entries.AddResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.BooleanResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.ConversationsListResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.PMMessageResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PMRetrofitApi {
    @GET("/pm/ConversationsList/page/{page}/appkey/$APP_KEY")
    fun getConversations(@Path("page") page : Int) : Single<List<ConversationsListResponse>>

    @GET("/pm/Conversation/{user}/appkey/$APP_KEY")
    fun getConversation(@Path("user") user : String) : Single<List<PMMessageResponse>>
    @Multipart
    @POST("/pm/SendMessage/{user}/appkey/$APP_KEY")
    fun sendMessage(@Part("body") body: RequestBody,
                        @Path("user") user : String,
                        @Part file : MultipartBody.Part) : Single<AddResponse>

    @FormUrlEncoded
    @POST("/pm/SendMessage/{user}/appkey/$APP_KEY")
    fun sendMessage(@Field("body") body: String,
                        @Path("user") user : String,
                        @Field("embed") embed : String?) : Single<AddResponse>

    @GET("/pm/DeleteConversation/{user}/appkey/$APP_KEY")
    fun deleteConversation(@Path("user") user : String) : Single<BooleanResponse>
}