package io.github.feelfreelinux.wykopmobilny.ui.fragments.entries

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry

interface EntryActionListener {
    fun voteEntry(entry: Entry)
    fun unvoteEntry(entry: Entry)
    fun markFavorite(entry: Entry)
    fun deleteEntry(entry: Entry)
    fun voteSurvey(entry: Entry, index: Int)
    fun getVoters(entry: Entry)
}