package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.NotificationResponse

class NotificationMapper {
    companion object : Mapper<NotificationResponse, Notification> {
        override fun map(value: NotificationResponse): Notification {
            return Notification(
                    value.id,
                    if (value.author != null) AuthorMapper.map(value.author!!) else null,
                    value.body,
                    value.date,
                    value.type,
                    value.url,
                    value.new)
        }
    }
}