package io.github.feelfreelinux.wykopmobilny.api

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
            embed,
            voteCount,
            commentCount,
            commentList.toList()
    )
}


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
            voteCount
    )

fun TagEntriesResponse.mapToTagEntries() : TagEntries
    = TagEntries(meta.mapToTagMeta(), items.map { it.mapToEntry() })

fun TagMetaResponse.mapToTagMeta() : TagMeta
    = TagMeta(tag, isObserved ?: false, isObserved ?: false)