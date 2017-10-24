package io.github.feelfreelinux.wykopmobilny.api.pm

import io.github.feelfreelinux.wykopmobilny.api.entries.AddResponse
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesRetrofitApi
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.api.getRequestBody
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Embed
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.models.mapper.ConverstationMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.PMMessageMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.BooleanResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.ConversationsListResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.PMMessageResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit

interface PMApi {
    fun getConversations(page : Int): Single<List<Conversation>>
    fun getConversation(user : String): Single<List<PMMessage>>
    fun deleteConversation(user : String): Single<BooleanResponse>
    fun sendMessage(body : String, user : String, embed: String?): Single<Boolean>
    fun sendMessage(body : String, user : String, embed: TypedInputStream): Single<Boolean>
}

class PMRepository(val retrofit: Retrofit) : PMApi {
    private val pmretrofitApi by lazy { retrofit.create(PMRetrofitApi::class.java) }

    override fun getConversations(page : Int) = pmretrofitApi.getConversations(page).map { it.map { ConverstationMapper.map(it) } }

    override fun getConversation(user : String) = pmretrofitApi.getConversation(user).map { it.map { PMMessageMapper.map(it) } }

    override fun deleteConversation(user : String) = pmretrofitApi.deleteConversation(user)

    override fun sendMessage(body : String, user : String, embed: String?) = pmretrofitApi.sendMessage(body, user, embed).map { it.first() }

    override fun sendMessage(body : String, user : String, embed: TypedInputStream) =
            pmretrofitApi.sendMessage(body.toRequestBody(),
                    user,
                    MultipartBody.Part.createFormData(
                            "embed",
                            embed.fileName,
                            embed.inputStream.getRequestBody(embed.mimeType))!!).map { it.first() }

    private fun String.toRequestBody() = RequestBody.create(MultipartBody.FORM, this)!!
}