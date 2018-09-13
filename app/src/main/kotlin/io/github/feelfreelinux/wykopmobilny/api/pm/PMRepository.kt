package io.github.feelfreelinux.wykopmobilny.api.pm

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandler
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.ConversationMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.FullConversationMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.PMMessageMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ConversationDeleteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ConversationResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.PMMessageResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.FullConversationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit

class PMRepository(val retrofit: Retrofit, val userTokenRefresher: UserTokenRefresher) : PMApi {
    private val pmretrofitApi by lazy { retrofit.create(PMRetrofitApi::class.java) }

    override fun getConversations() = pmretrofitApi.getConversations()
            .retryWhen(userTokenRefresher)
            .compose<List<ConversationResponse>>(ErrorHandlerTransformer())
            .map { it.map { ConversationMapper.map(it) } }

    override fun getConversation(user : String) = pmretrofitApi.getConversation(user)
            .retryWhen(userTokenRefresher)
            .flatMap<FullConversationResponse>(ErrorHandler())
            .map { FullConversationMapper.map(it) }

    override fun deleteConversation(user : String)
    = pmretrofitApi.deleteConversation(user)
            .retryWhen(userTokenRefresher)
            .compose<ConversationDeleteResponse>(ErrorHandlerTransformer())

    override fun sendMessage(body : String, user : String, embed: String?, plus18: Boolean) =
            pmretrofitApi.sendMessage(body, user, embed, plus18)
                    .retryWhen(userTokenRefresher)
                    .compose<PMMessageResponse>(ErrorHandlerTransformer())
                    .map { PMMessageMapper.map(it) }

    override fun sendMessage(body : String, user : String, plus18 : Boolean, embed: WykopImageFile) =
            pmretrofitApi.sendMessage(body.toRequestBody(),
                    plus18.toRequestBody(),
                    user,
                    embed.getFileMultipart())
                    .retryWhen(userTokenRefresher)
                    .compose<PMMessageResponse>(ErrorHandlerTransformer())
                    .map { PMMessageMapper.map(it) }
    private fun Boolean.toRequestBody() = RequestBody.create(MultipartBody.FORM, if (this) "1" else "")!!
    private fun String.toRequestBody() = RequestBody.create(MultipartBody.FORM, this)!!
}