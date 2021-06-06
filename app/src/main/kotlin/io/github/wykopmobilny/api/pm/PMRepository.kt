package io.github.wykopmobilny.api.pm

import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.WykopImageFile
import io.github.wykopmobilny.api.endpoints.PMRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandler
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.api.patrons.PatronsApi
import io.github.wykopmobilny.models.mapper.apiv2.ConversationMapper
import io.github.wykopmobilny.models.mapper.apiv2.FullConversationMapper
import io.github.wykopmobilny.models.mapper.apiv2.PMMessageMapper
import kotlinx.coroutines.rx2.rxSingle
import toRequestBody
import javax.inject.Inject

class PMRepository @Inject constructor(
    private val pmRetrofitApi: PMRetrofitApi,
    private val userTokenRefresher: UserTokenRefresher,
    private val patronsApi: PatronsApi,
) : PMApi {

    override fun getConversations() = rxSingle { pmRetrofitApi.getConversations() }
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> ConversationMapper.map(response) } }

    override fun getConversation(user: String) = rxSingle { pmRetrofitApi.getConversation(user) }
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .flatMap(ErrorHandler())
        .map { FullConversationMapper.map(it) }

    override fun deleteConversation(user: String) = rxSingle { pmRetrofitApi.deleteConversation(user) }
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose(ErrorHandlerTransformer())

    override fun sendMessage(body: String, user: String, embed: String?, plus18: Boolean) =
        rxSingle { pmRetrofitApi.sendMessage(body, user, embed, plus18) }
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { PMMessageMapper.map(it) }

    override fun sendMessage(body: String, user: String, plus18: Boolean, embed: WykopImageFile) =
        rxSingle {
            pmRetrofitApi.sendMessage(
                body = body.toRequestBody(),
                plus18 = plus18.toRequestBody(),
                user = user,
                file = embed.getFileMultipart(),
            )
        }
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { PMMessageMapper.map(it) }
}
