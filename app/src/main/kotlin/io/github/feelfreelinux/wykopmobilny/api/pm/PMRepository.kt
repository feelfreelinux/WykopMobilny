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

class PMRepository(
    val retrofit: Retrofit,
    val userTokenRefresher: UserTokenRefresher
) : PMApi {

    private val pmRetrofitApi by lazy { retrofit.create(PMRetrofitApi::class.java) }

    override fun getConversations() = pmRetrofitApi.getConversations()
        .retryWhen(userTokenRefresher)
        .compose<List<ConversationResponse>>(ErrorHandlerTransformer())
        .map { it.map { response -> ConversationMapper.map(response) } }

    override fun getConversation(user: String) = pmRetrofitApi.getConversation(user)
        .retryWhen(userTokenRefresher)
        .flatMap<FullConversationResponse>(ErrorHandler())
        .map { FullConversationMapper.map(it) }

    override fun deleteConversation(user: String) = pmRetrofitApi.deleteConversation(user)
        .retryWhen(userTokenRefresher)
        .compose<ConversationDeleteResponse>(ErrorHandlerTransformer())

    override fun sendMessage(body: String, user: String, embed: String?, plus18: Boolean) =
        pmRetrofitApi.sendMessage(body, user, embed, plus18)
            .retryWhen(userTokenRefresher)
            .compose<PMMessageResponse>(ErrorHandlerTransformer())
            .map { PMMessageMapper.map(it) }

    override fun sendMessage(body: String, user: String, plus18: Boolean, embed: WykopImageFile) =
        pmRetrofitApi.sendMessage(
            body.toRequestBody(),
            plus18.toRequestBody(),
            user,
            embed.getFileMultipart()
        )
            .retryWhen(userTokenRefresher)
            .compose<PMMessageResponse>(ErrorHandlerTransformer())
            .map { PMMessageMapper.map(it) }

    private fun Boolean.toRequestBody() = RequestBody.create(MultipartBody.FORM, if (this) "1" else "")!!

    private fun String.toRequestBody() = RequestBody.create(MultipartBody.FORM, this)!!
}