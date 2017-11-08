package io.github.feelfreelinux.wykopmobilny.api.pm

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ConversationDeleteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ConversationResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.PMMessageResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.FullConversationResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PMRetrofitApi {
    @GET("/pm/ConversationsList/appkey/$APP_KEY")
    fun getConversations() : Single<WykopApiResponse<List<ConversationResponse>>>

    @GET("/pm/Conversation/{user}/appkey/$APP_KEY")
    fun getConversation(@Path("user") user : String) : Single<FullConversationResponse>

    @Multipart
    @POST("/pm/SendMessage/{user}/appkey/$APP_KEY")
    fun sendMessage(@Part("body") body: RequestBody,
                        @Path("user") user : String,
                        @Part file : MultipartBody.Part) : Single<WykopApiResponse<PMMessageResponse>>

    @FormUrlEncoded
    @POST("/pm/SendMessage/{user}/appkey/$APP_KEY")
    fun sendMessage(@Field("body") body: String,
                        @Path("user") user : String,
                        @Field("embed") embed : String?) : Single<WykopApiResponse<PMMessageResponse>>

    @GET("/pm/DeleteConversation/{user}/appkey/$APP_KEY")
    fun deleteConversation(@Path("user") user : String) : Single<WykopApiResponse<ConversationDeleteResponse>>
}