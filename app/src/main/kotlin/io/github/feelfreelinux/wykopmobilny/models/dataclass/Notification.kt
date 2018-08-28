package io.github.feelfreelinux.wykopmobilny.models.dataclass

open class Notification(
        val id : Int,
        val author : Author?,
        val body : String,
        val date : String,
        val type : String,
        val url : String?,
        var new : Boolean
) {
    var visible = true

    override fun equals(other: Any?): Boolean {
        return if (other !is Notification) false
        else (other.id == id)
    }

    val tag by lazy { this.body.substringAfter("#").substringBefore(" ") }

    override fun hashCode(): Int {
        return id
    }
}