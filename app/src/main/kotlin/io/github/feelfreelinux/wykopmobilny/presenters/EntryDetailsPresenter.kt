package io.github.feelfreelinux.wykopmobilny.presenters

import io.github.feelfreelinux.wykopmobilny.adapters.holders.EntryDetailsListItem
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.utils.ApiResponseCallback
import io.github.feelfreelinux.wykopmobilny.utils.VoteResponseListener
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate

interface EntryDetailsCallbacks {
    fun onTagClicked(tag: String)
    fun onVote(type: String, id: Int, commentId: Int?, responseCallback: VoteResponseListener)
    fun onUnvote(type: String, id: Int, commentId: Int?, responseCallback: VoteResponseListener)
    fun onProfileClicked(login: String)
}

class EntryDetailsPresenter(val wam: WykopApiManager, val callbacks: EntryDetailsCallbacks) {
    var entry: Entry? = null
    var dataLoadedCallback = {}

    val responseCallback: ApiResponseCallback = {
        entry = it as Entry
        dataLoadedCallback()
    }

    fun onBindListItem(position: Int, listItem: EntryDetailsListItem) {
        entry?.let {
            // Set tag clicked listener
            listItem.tagClickListener = { tag -> callbacks.onTagClicked(tag) }

            if (position == 0) bindEntry(listItem)
            else bindComment(position - 1, listItem)
        }
    }

    fun bindEntry(listItem: EntryDetailsListItem) {
        entry?.let {
            val entry = entry as Entry
            listItem.setBody(entry.body, entry.embed)
            listItem.setHeader(entry.author,
                    entry.authorGroup,
                    entry.authorSex,
                    entry.authorAvatarMed,
                    entry.date.toPrettyDate(),
                    entry.app)
            listItem.setupVoteButton(entry.voteCount, entry.userVote > 0)

            // Setup vote actions
            listItem.voteClickListener = {
                if (entry.userVote > 0)
                    callbacks.onUnvote("entry", entry.id, null, {
                        (vote) ->
                        listItem.setupVoteButton(vote, false)
                        entry.userVote = 0
                    })
                else {
                    callbacks.onVote("entry", entry.id, null, {
                        (vote) ->
                        listItem.setupVoteButton(vote, true)
                        entry.userVote = 1
                    })
                }
            }

            if (entry.embed != null) listItem.setupEmbed(entry.embed!!) else listItem.hideImage()
        }
    }

    fun bindComment(position: Int, listItem: EntryDetailsListItem) {
        entry?.let {
            val comment = entry!!.comments!![position]

            listItem.setBody(comment.body, comment.embed)
            listItem.setHeader(comment.author,
                    comment.authorGroup,
                    comment.authorSex,
                    comment.authorAvatarMed,
                    comment.date.toPrettyDate(),
                    comment.app)
            listItem.setupVoteButton(comment.voteCount, comment.userVote > 0)

            // Setup vote actions
            listItem.voteClickListener = {
                if (comment.userVote > 0)
                    callbacks.onUnvote("comment", comment.entryId, comment.id, {
                        (vote) ->
                        listItem.setupVoteButton(vote, false)
                        comment.userVote = 0
                    })
                else {
                    callbacks.onVote("comment", comment.entryId, comment.id, {
                        (vote) ->
                        listItem.setupVoteButton(vote, true)
                        comment.userVote = 1
                    })
                }
            }

            if (comment.embed != null) listItem.setupEmbed(comment.embed!!) else listItem.hideImage()
        }
    }

    fun getItemsCount(): Int {
        entry?.let {
            return it.comments!!.size + 1
        }
        return 0
    }

    fun loadData(entryId: Int) =
            wam.getEntryIndex(entryId, responseCallback)

}