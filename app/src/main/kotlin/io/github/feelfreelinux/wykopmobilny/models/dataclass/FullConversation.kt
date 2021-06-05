package io.github.feelfreelinux.wykopmobilny.models.dataclass

data class FullConversation(
    val messages: List<PMMessage>,
    val receiver: Author
)
