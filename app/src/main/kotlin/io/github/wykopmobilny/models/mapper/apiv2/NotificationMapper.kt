package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.NotificationResponse
import io.github.wykopmobilny.models.dataclass.Notification
import io.github.wykopmobilny.models.mapper.Mapper

class NotificationMapper {
    companion object : Mapper<NotificationResponse, Notification> {
        override fun map(value: NotificationResponse) =
            Notification(
                value.id,
                if (value.author != null) AuthorMapper.map(value.author!!) else null,
                value.body,
                value.date,
                value.type,
                value.url,
                value.new
            )
    }
}
