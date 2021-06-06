package io.github.wykopmobilny.api.endpoints

import io.github.wykopmobilny.APP_KEY
import io.github.wykopmobilny.api.responses.ConversationDeleteResponse
import io.github.wykopmobilny.api.responses.ConversationResponse
import io.github.wykopmobilny.api.responses.FullConversationResponse
import io.github.wykopmobilny.api.responses.PMMessageResponse
import io.github.wykopmobilny.api.responses.WykopApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PMRetrofitApi {

    @GET("/pm/ConversationsList/appkey/$APP_KEY")
    suspend fun getConversations(): WykopApiResponse<List<ConversationResponse>>

    @GET("/pm/Conversation/{user}/appkey/$APP_KEY")
    suspend fun getConversation(@Path("user") user: String): FullConversationResponse

    @Multipart
    @POST("/pm/SendMessage/{user}/appkey/$APP_KEY")
    suspend fun sendMessage(
        @Part("body") body: RequestBody,
        @Part("adultmedia") plus18: RequestBody,
        @Path("user") user: String,
        @Part file: MultipartBody.Part
    ): WykopApiResponse<PMMessageResponse>

    @FormUrlEncoded
    @POST("/pm/SendMessage/{user}/appkey/$APP_KEY")
    suspend fun sendMessage(
        @Field("body") body: String,
        @Path("user") user: String,
        @Field("embed") embed: String?,
        @Field("adultmedia") plus18: Boolean
    ): WykopApiResponse<PMMessageResponse>

    @GET("/pm/DeleteConversation/{user}/appkey/$APP_KEY")
    suspend fun deleteConversation(@Path("user") user: String): WykopApiResponse<ConversationDeleteResponse>
}
