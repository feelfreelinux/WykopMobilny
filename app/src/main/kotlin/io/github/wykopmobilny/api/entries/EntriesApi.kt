package io.github.wykopmobilny.api.entries

import io.github.wykopmobilny.api.WykopImageFile
import io.github.wykopmobilny.api.responses.EntryCommentResponse
import io.github.wykopmobilny.api.responses.EntryResponse
import io.github.wykopmobilny.api.responses.FavoriteResponse
import io.github.wykopmobilny.api.responses.VoteResponse
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.models.dataclass.EntryVotePublishModel
import io.github.wykopmobilny.models.dataclass.Survey
import io.github.wykopmobilny.models.dataclass.Voter
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface EntriesApi {

    val entryVoteSubject: PublishSubject<EntryVotePublishModel>
    val entryUnVoteSubject: PublishSubject<EntryVotePublishModel>

    fun voteEntry(entryId: Int, notifySubject: Boolean = true): Single<VoteResponse>
    fun unvoteEntry(entryId: Int, notifySubject: Boolean = true): Single<VoteResponse>
    fun voteComment(commentId: Int): Single<VoteResponse>
    fun unvoteComment(commentId: Int): Single<VoteResponse>
    fun addEntry(body: String, wykopImageFile: WykopImageFile, plus18: Boolean): Single<EntryResponse>
    fun addEntry(body: String, embed: String?, plus18: Boolean): Single<EntryResponse>
    fun addEntryComment(body: String, entryId: Int, embed: String?, plus18: Boolean): Single<EntryCommentResponse>
    fun addEntryComment(body: String, entryId: Int, wykopImageFile: WykopImageFile, plus18: Boolean): Single<EntryCommentResponse>
    fun markFavorite(entryId: Int): Single<FavoriteResponse>
    fun deleteEntry(entryId: Int): Single<EntryResponse>
    fun editEntry(body: String, entryId: Int): Single<EntryResponse>
    fun editEntryComment(body: String, commentId: Int): Single<EntryCommentResponse>
    fun deleteEntryComment(commentId: Int): Single<EntryCommentResponse>
    fun voteSurvey(entryId: Int, answerId: Int): Single<Survey>

    fun getHot(page: Int, period: String): Single<List<Entry>>
    fun getStream(page: Int): Single<List<Entry>>
    fun getActive(page: Int): Single<List<Entry>>
    fun getObserved(page: Int): Single<List<Entry>>
    fun getEntry(id: Int): Single<Entry>
    fun getEntryVoters(id: Int): Single<List<Voter>>
    fun getEntryCommentVoters(id: Int): Single<List<Voter>>
}
