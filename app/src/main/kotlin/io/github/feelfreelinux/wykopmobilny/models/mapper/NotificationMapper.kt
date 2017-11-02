package io.github.feelfreelinux.wykopmobilny.models.mapper

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.pojo.NotificationResponse

class NotificationMapper {
    companion object : Mapper<NotificationResponse, Notification> {
        override fun map(value: NotificationResponse): Notification {
            value.run {
                val author = if (author != null && authorAvatar != null && authorGroup != null && authorSex != null)
                    Author(author, authorAvatar, authorGroup, authorSex, null)
                else null

                return Notification(
                        author,
                        body,
                        date,
                        type,
                        url,
                        new)
            }
        }
    }
}