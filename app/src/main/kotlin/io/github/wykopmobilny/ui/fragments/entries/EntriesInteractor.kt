package io.github.wykopmobilny.ui.fragments.entries

import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.models.dataclass.Entry
import io.reactivex.Single
import javax.inject.Inject

class EntriesInteractor @Inject constructor(val entriesApi: EntriesApi) {

    fun voteEntry(entry: Entry): Single<Entry> =
        entriesApi.voteEntry(entry.id, true)
            .map {
                entry.voteCount = it.voteCount
                entry.isVoted = true
                entry
            }

    fun unvoteEntry(entry: Entry): Single<Entry> =
        entriesApi.unvoteEntry(entry.id, true)
            .map {
                entry.voteCount = it.voteCount
                entry.isVoted = false
                entry
            }

    fun markFavorite(entry: Entry): Single<Entry> =
        entriesApi.markFavorite(entry.id)
            .map {
                entry.isFavorite = it.userFavorite
                entry
            }

    fun deleteEntry(entry: Entry): Single<Entry> =
        entriesApi.deleteEntry(entry.id)
            .map {
                entry.embed = null
                entry.survey = null
                entry.body = "[Wpis usunięty]"
                entry
            }

    fun voteSurvey(entry: Entry, index: Int): Single<Entry> =
        entriesApi.voteSurvey(entry.id, index)
            .map {
                entry.survey = it
                entry
            }
}
