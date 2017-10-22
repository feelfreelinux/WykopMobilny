package io.github.feelfreelinux.wykopmobilny.models.dataclass

data class Conversation(
        val user : Author,
        val lastUpdate : String,
        val isNew : Boolean
)