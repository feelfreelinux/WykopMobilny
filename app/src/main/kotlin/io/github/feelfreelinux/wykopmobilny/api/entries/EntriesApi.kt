package io.github.feelfreelinux.wykopmobilny.api.entries

import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryVotePublishModel
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Survey
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryCommentResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.FavoriteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.VoteResponse
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject

interface EntriesApi {
    val entryVoteSubject : PublishSubject<EntryVotePublishModel>
    val entryUnVoteSubject : PublishSubject<EntryVotePublishModel>
    fun voteEntry(entryId: Int, notifySubject : Boolean = true): Single<VoteResponse>
    fun unvoteEntry(entryId: Int, notifySubject : Boolean = true): Single<VoteResponse>
    fun voteComment(commentId: Int): Single<VoteResponse>
    fun unvoteComment(commentId: Int): Single<VoteResponse>
    fun addEntry(body : String, wykopImageFile: WykopImageFile, plus18: Boolean): Single<EntryResponse>
    fun addEntry(body: String, embed: String?, plus18: Boolean): Single<EntryResponse>
    fun addEntryComment(body: String, entryId: Int, embed: String?, plus18: Boolean): Single<EntryCommentResponse>
    fun addEntryComment(body: String, entryId: Int, wykopImageFile: WykopImageFile, plus18: Boolean): Single<EntryCommentResponse>
    fun markFavorite(entryId: Int) : Single<FavoriteResponse>
    fun deleteEntry(entryId: Int): Single<EntryResponse>
    fun editEntry(body: String, entryId: Int): Single<EntryResponse>
    fun editEntryComment(body : String, commentId: Int): Single<EntryCommentResponse>
    fun deleteEntryComment(commentId: Int): Single<EntryCommentResponse>
    fun voteSurvey(entryId: Int, answerId : Int): Single<Survey>


    fun getHot(page : Int, period : String) : Single<List<Entry>>
    fun getStream(page : Int) : Single<List<Entry>>
    fun getActive(page : Int) : Single<List<Entry>>
    fun getObserved(page : Int) : Single<List<Entry>>
    fun getEntry(id : Int) : Single<Entry>
    fun getEntryVoters(id : Int) : Single<List<Voter>>
    fun getEntryCommentVoters(id : Int) : Single<List<Voter>>

}