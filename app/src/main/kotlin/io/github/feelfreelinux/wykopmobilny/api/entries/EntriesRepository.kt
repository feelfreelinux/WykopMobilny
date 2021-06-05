package io.github.feelfreelinux.wykopmobilny.api.entries

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.api.patrons.PatronsApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryVotePublishModel
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.EntryMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.SurveyMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.VoterMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryCommentResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.FavoriteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.SurveyResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.VoteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.VoterResponse
import io.reactivex.subjects.PublishSubject
import retrofit2.Retrofit
import toRequestBody

class EntriesRepository(
    val retrofit: Retrofit,
    val userTokenRefresher: UserTokenRefresher,
    val owmContentFilter: OWMContentFilter,
    val patronsApi: PatronsApi
) : EntriesApi {

    private val entriesApi by lazy { retrofit.create(EntriesRetrofitApi::class.java) }

    override val entryVoteSubject = PublishSubject.create<EntryVotePublishModel>()
    override val entryUnVoteSubject = PublishSubject.create<EntryVotePublishModel>()

    override fun voteEntry(entryId: Int, notifySubject: Boolean) = entriesApi.voteEntry(entryId)
        .retryWhen(userTokenRefresher)
        .compose<VoteResponse>(ErrorHandlerTransformer())
        .doOnSuccess {
            if (notifySubject) entryVoteSubject.onNext(EntryVotePublishModel(entryId, it))
        }

    override fun unvoteEntry(entryId: Int, notifySubject: Boolean) = entriesApi.unvoteEntry(entryId)
        .retryWhen(userTokenRefresher)
        .compose<VoteResponse>(ErrorHandlerTransformer())
        .doOnSuccess {
            if (notifySubject) entryUnVoteSubject.onNext(EntryVotePublishModel(entryId, it))
        }

    override fun voteComment(commentId: Int) = entriesApi.voteComment(commentId)
        .retryWhen(userTokenRefresher)
        .compose<VoteResponse>(ErrorHandlerTransformer())

    override fun unvoteComment(commentId: Int) = entriesApi.unvoteComment(commentId)
        .retryWhen(userTokenRefresher)
        .compose<VoteResponse>(ErrorHandlerTransformer())

    override fun addEntry(body: String, wykopImageFile: WykopImageFile, plus18: Boolean) =
        entriesApi.addEntry(body.toRequestBody(), plus18.toRequestBody(), wykopImageFile.getFileMultipart())
            .retryWhen(userTokenRefresher)
            .compose<EntryResponse>(ErrorHandlerTransformer())

    override fun addEntry(body: String, embed: String?, plus18: Boolean) = entriesApi.addEntry(body, embed, plus18)
        .retryWhen(userTokenRefresher)
        .compose<EntryResponse>(ErrorHandlerTransformer())

    override fun addEntryComment(body: String, entryId: Int, wykopImageFile: WykopImageFile, plus18: Boolean) =
        entriesApi.addEntryComment(
            body.toRequestBody(),
            plus18.toRequestBody(),
            entryId,
            wykopImageFile.getFileMultipart()
        )
            .retryWhen(userTokenRefresher)
            .compose<EntryCommentResponse>(ErrorHandlerTransformer())

    override fun addEntryComment(body: String, entryId: Int, embed: String?, plus18: Boolean) =
        entriesApi.addEntryComment(body, embed, plus18, entryId)
            .retryWhen(userTokenRefresher)
            .compose<EntryCommentResponse>(ErrorHandlerTransformer())

    override fun editEntry(body: String, entryId: Int) = entriesApi.editEntry(body, entryId)
        .retryWhen(userTokenRefresher)
        .compose<EntryResponse>(ErrorHandlerTransformer())

    override fun markFavorite(entryId: Int) = entriesApi.markFavorite(entryId)
        .retryWhen(userTokenRefresher)
        .compose<FavoriteResponse>(ErrorHandlerTransformer())

    override fun deleteEntry(entryId: Int) = entriesApi.deleteEntry(entryId)
        .retryWhen(userTokenRefresher)
        .compose<EntryResponse>(ErrorHandlerTransformer())

    override fun editEntryComment(body: String, commentId: Int) = entriesApi.editEntryComment(body, commentId)
        .retryWhen(userTokenRefresher)
        .compose<EntryCommentResponse>(ErrorHandlerTransformer())

    override fun deleteEntryComment(commentId: Int) = entriesApi.deleteEntryComment(commentId)
        .retryWhen(userTokenRefresher)
        .compose<EntryCommentResponse>(ErrorHandlerTransformer())

    override fun voteSurvey(entryId: Int, answerId: Int) = entriesApi.voteSurvey(entryId, answerId)
        .retryWhen(userTokenRefresher)
        .compose<SurveyResponse>(ErrorHandlerTransformer())
        .map { SurveyMapper.map(it) }

    override fun getHot(page: Int, period: String) = entriesApi.getHot(page, period)
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose<List<EntryResponse>>(ErrorHandlerTransformer())
        .map { it.map { response -> EntryMapper.map(response, owmContentFilter) } }

    override fun getStream(page: Int) = entriesApi.getStream(page)
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose<List<EntryResponse>>(ErrorHandlerTransformer())
        .map { it.map { response -> EntryMapper.map(response, owmContentFilter) } }

    override fun getActive(page: Int) = entriesApi.getActive(page)
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose<List<EntryResponse>>(ErrorHandlerTransformer())
        .map { it.map { response -> EntryMapper.map(response, owmContentFilter) } }

    override fun getObserved(page: Int) = entriesApi.getObserved(page)
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose<List<EntryResponse>>(ErrorHandlerTransformer())
        .map { it.map { response -> EntryMapper.map(response, owmContentFilter) } }

    override fun getEntry(id: Int) = entriesApi.getEntry(id)
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose<EntryResponse>(ErrorHandlerTransformer())
        .map { EntryMapper.map(it, owmContentFilter) }

    override fun getEntryVoters(id: Int) = entriesApi.getEntryVoters(id)
        .retryWhen(userTokenRefresher)
        .compose<List<VoterResponse>>(ErrorHandlerTransformer())
        .map { it.map { response -> VoterMapper.map(response) } }

    override fun getEntryCommentVoters(id: Int) = entriesApi.getCommentUpvoters(id)
        .retryWhen(userTokenRefresher)
        .compose<List<VoterResponse>>(ErrorHandlerTransformer())
        .map { it.map { response -> VoterMapper.map(response) } }
}
