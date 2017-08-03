package io.github.feelfreelinux.wykopmobilny.presenters

import io.github.feelfreelinux.wykopmobilny.adapters.holders.FeedViewItem
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.objects.VoteResponse
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate

interface FeedViewCallbacks {
    fun onTagClicked(tag : String)
    fun onVote(type : String, id: Int, commentId : Int?, responseCallback : (VoteResponse) -> Unit)
    fun onUnvote(type : String, id: Int, commentId : Int?, responseCallback : (VoteResponse) -> Unit)
    fun onCommentsClicked(entryId : Int)
    fun onProfileClicked(login : String)
}

abstract class FeedPresenter(val wam : WykopApiManager, val callbacks: FeedViewCallbacks) {
    var entryList = ArrayList<Entry>()
    var dataLoadedListener = {}

    fun onBindListItem(position : Int, listItem : FeedViewItem) {
        // Set tag clicked listener
        listItem.tagClickListener = { tag -> callbacks.onTagClicked(tag) }

        val entry = entryList[position]
        listItem.setBody(entry.body, entry.embed)
        listItem.setHeader(entry.author,
                entry.authorGroup,
                entry.authorSex,
                entry.authorAvatarMed,
                entry.date.toPrettyDate(),
                entry.app)
        listItem.setupVoteButton(entry.voteCount, entry.userVote > 0)
        listItem.commentClickListener = {
            callbacks.onCommentsClicked(entry.id)
        }
        listItem.setupCommentsButton(entry.commentCount)

        // Setup vote actions
        listItem.voteClickListener = {
            if (entry.userVote > 0)
                callbacks.onUnvote("entry", entry.id, null, {
                    (vote) -> listItem.setupVoteButton(vote, false)
                    entry.userVote = 0
                })
            else {
                callbacks.onVote("entry", entry.id, null, {
                    (vote) -> listItem.setupVoteButton(vote, true)
                    entry.userVote = 1
                })
            }
        }

        // Setup embed
        if (entry.embed != null) listItem.setupEmbed(entry.embed!!) else listItem.hideImage()
    }

    fun getItemsCount() : Int = entryList.size

    abstract fun loadData(page : Int)
}