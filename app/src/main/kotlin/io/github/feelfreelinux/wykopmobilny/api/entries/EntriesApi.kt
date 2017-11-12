package io.github.feelfreelinux.wykopmobilny.api.entries

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Survey
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryCommentResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.FavoriteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.VoteResponse
import io.reactivex.Single

interface EntriesApi {
    fun voteEntry(entryId: Int): Single<VoteResponse>
    fun unvoteEntry(entryId: Int): Single<VoteResponse>
    fun voteComment(commentId: Int): Single<VoteResponse>
    fun unvoteComment(commentId: Int): Single<VoteResponse>
    fun addEntry(body : String, inputStream: TypedInputStream): Single<EntryResponse>
    fun addEntry(body: String, embed: String?): Single<EntryResponse>
    fun addEntryComment(body: String, entryId: Int, embed: String?): Single<EntryCommentResponse>
    fun addEntryComment(body: String, entryId: Int, inputStream: TypedInputStream): Single<EntryCommentResponse>
    fun markFavorite(entryId: Int) : Single<FavoriteResponse>
    fun deleteEntry(entryId: Int): Single<EntryResponse>
    fun editEntry(body: String, entryId: Int): Single<EntryResponse>
    fun editEntryComment(body : String, commentId: Int): Single<EntryCommentResponse>
    fun deleteEntryComment(commentId: Int): Single<EntryCommentResponse>
    fun voteSurvey(entryId: Int, answerId : Int): Single<Survey>


    fun getHot(page : Int, period : String) : Single<List<Entry>>
    fun getStream(page : Int) : Single<List<Entry>>
    fun getEntry(id : Int) : Single<Entry>

}