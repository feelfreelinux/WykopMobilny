package io.github.feelfreelinux.wykopmobilny.models.dataclass

class NotificationHeader(
        body : String,
        var notificationsCount : Int) : Notification(0, null, body, "", "header", "", false) {
    override fun equals(other: Any?): Boolean {
        return if (other !is NotificationHeader) false
        else (other.body == body)
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }
}