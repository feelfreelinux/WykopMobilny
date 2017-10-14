package io.github.feelfreelinux.wykopmobilny.models

import io.github.feelfreelinux.wykopmobilny.models.dataclass.*
import io.github.feelfreelinux.wykopmobilny.models.pojo.*

fun EntryResponse.mapToEntry() : Entry {
    val commentList = ArrayList<Comment>()
    comments?.mapTo(commentList, { it.mapToComment() })
    return Entry(
            id,
            Author(
                    author,
                    authorAvatarMed,
                    authorGroup,
                    authorSex,
                    app
            ),
            body,
            date,
            userVote > 0,
            !(userFavorite == null || !userFavorite!!),
            embed,
            voteCount,
            voters.map { Author(it.author, it.authorAvatarMed, it.authorGroup, it.authorSex, null) },
            commentCount,
            commentList.toList()
    )
}

fun NotificationResponse.mapToNotification() : Notification
    = Notification(
        Author(author ?: "", authorAvatar, authorGroup, authorSex, null),
        body,
        date,
        url,
        new
)

fun CommentResponse.mapToComment() : Comment
    = Comment(
        id,
        entryId,
        Author(
                author,
                authorAvatarMed,
                authorGroup,
                authorSex,
                app
        ),
        body,
        date,
        userVote > 0,
        embed,
        voters.map { Author(it.author, it.authorAvatarMed, it.authorGroup, it.authorSex, null) },
        voteCount
)

fun TagEntriesResponse.mapToTagEntries() : TagEntries
    = TagEntries(meta.mapToTagMeta(), items.map { it.mapToEntry() })

fun TagMetaResponse.mapToTagMeta() : TagMeta
    = TagMeta(tag, isObserved ?: false, isObserved ?: false)