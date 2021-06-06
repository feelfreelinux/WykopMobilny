package io.github.wykopmobilny.api.pm

import io.github.wykopmobilny.api.WykopImageFile
import io.github.wykopmobilny.api.responses.ConversationDeleteResponse
import io.github.wykopmobilny.models.dataclass.Conversation
import io.github.wykopmobilny.models.dataclass.FullConversation
import io.github.wykopmobilny.models.dataclass.PMMessage
import io.reactivex.Single

interface PMApi {
    fun getConversations(): Single<List<Conversation>>
    fun getConversation(user: String): Single<FullConversation>
    fun deleteConversation(user: String): Single<ConversationDeleteResponse>
    fun sendMessage(body: String, user: String, embed: String?, plus18: Boolean): Single<PMMessage>
    fun sendMessage(body: String, user: String, plus18: Boolean, embed: WykopImageFile): Single<PMMessage>
}
