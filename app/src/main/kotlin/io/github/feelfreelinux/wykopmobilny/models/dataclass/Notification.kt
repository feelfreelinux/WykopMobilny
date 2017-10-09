package io.github.feelfreelinux.wykopmobilny.models.dataclass

data class Notification(
        val author : Author,
        val body : String,
        val date : String,
        val url : String,
        var new : Boolean
)