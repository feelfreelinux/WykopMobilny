package io.github.feelfreelinux.wykopmobilny.models.dataclass

class Notification(
        val id : Int,
        val author : Author?,
        val body : String,
        val date : String,
        val type : String,
        val url : String?,
        var new : Boolean
) {
    override fun equals(other: Any?): Boolean {
        return if (other !is Notification) false
        else (other.id == id)
    }

    override fun hashCode(): Int {
        return id
    }
}