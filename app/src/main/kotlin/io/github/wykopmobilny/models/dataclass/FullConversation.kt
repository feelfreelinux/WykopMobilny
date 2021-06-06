package io.github.wykopmobilny.models.dataclass

data class FullConversation(
    val messages: List<PMMessage>,
    val receiver: Author
)
