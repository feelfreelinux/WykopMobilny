package io.github.feelfreelinux.wykopmobilny.api.pm

import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.models.dataclass.FullConversation
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ConversationDeleteResponse
import io.reactivex.Single

interface PMApi {
    fun getConversations(): Single<List<Conversation>>
    fun getConversation(user : String): Single<FullConversation>
    fun deleteConversation(user : String): Single<ConversationDeleteResponse>
    fun sendMessage(body : String, user : String, embed: String?, plus18 : Boolean): Single<PMMessage>
    fun sendMessage(body : String, user : String, plus18: Boolean, embed: TypedInputStream): Single<PMMessage>
}