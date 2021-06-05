package io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.reactivex.Single
import javax.inject.Inject

class EntryCommentInteractor @Inject constructor(val entriesApi: EntriesApi) {

    fun voteComment(comment: EntryComment): Single<EntryComment> =
        entriesApi.voteComment(comment.id)
            .map {
                comment.voteCount = it.voteCount
                comment.isVoted = true
                comment
            }

    fun unvoteComment(comment: EntryComment): Single<EntryComment> =
        entriesApi.unvoteComment(comment.id)
            .map {
                comment.voteCount = it.voteCount
                comment.isVoted = false
                comment
            }

    fun deleteComment(comment: EntryComment): Single<EntryComment> =
        entriesApi.deleteEntryComment(comment.id)
            .map {
                comment.embed = null
                comment.body = "[Komentarz usunięty]"
                comment
            }
}
