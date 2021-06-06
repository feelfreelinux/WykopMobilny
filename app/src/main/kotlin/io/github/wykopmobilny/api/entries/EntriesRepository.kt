package io.github.wykopmobilny.api.entries

import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.WykopImageFile
import io.github.wykopmobilny.api.endpoints.EntriesRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.patrons.PatronsApi
import io.github.wykopmobilny.models.dataclass.EntryVotePublishModel
import io.github.wykopmobilny.models.mapper.apiv2.EntryMapper
import io.github.wykopmobilny.models.mapper.apiv2.SurveyMapper
import io.github.wykopmobilny.models.mapper.apiv2.VoterMapper
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.rx2.rxSingle
import toRequestBody
import javax.inject.Inject

class EntriesRepository @Inject constructor(
    private val entriesApi: EntriesRetrofitApi,
    private val userTokenRefresher: UserTokenRefresher,
    private val owmContentFilter: OWMContentFilter,
    private val patronsApi: PatronsApi
) : EntriesApi {

    override val entryVoteSubject = PublishSubject.create<EntryVotePublishModel>()
    override val entryUnVoteSubject = PublishSubject.create<EntryVotePublishModel>()

    override fun voteEntry(entryId: Int, notifySubject: Boolean) = rxSingle { entriesApi.voteEntry(entryId) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .doOnSuccess {
            if (notifySubject) entryVoteSubject.onNext(EntryVotePublishModel(entryId, it))
        }

    override fun unvoteEntry(entryId: Int, notifySubject: Boolean) = rxSingle { entriesApi.unvoteEntry(entryId) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .doOnSuccess {
            if (notifySubject) entryUnVoteSubject.onNext(EntryVotePublishModel(entryId, it))
        }

    override fun voteComment(commentId: Int) = rxSingle { entriesApi.voteComment(commentId) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun unvoteComment(commentId: Int) = rxSingle { entriesApi.unvoteComment(commentId) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun addEntry(body: String, wykopImageFile: WykopImageFile, plus18: Boolean) =
        rxSingle { entriesApi.addEntry(body.toRequestBody(), plus18.toRequestBody(), wykopImageFile.getFileMultipart()) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())

    override fun addEntry(body: String, embed: String?, plus18: Boolean) = rxSingle { entriesApi.addEntry(body, embed, plus18) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun addEntryComment(body: String, entryId: Int, wykopImageFile: WykopImageFile, plus18: Boolean) =
        rxSingle {
            entriesApi.addEntryComment(
                body = body.toRequestBody(),
                plus18 = plus18.toRequestBody(),
                entryId = entryId,
                file = wykopImageFile.getFileMultipart()
            )
        }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())

    override fun addEntryComment(body: String, entryId: Int, embed: String?, plus18: Boolean) =
        rxSingle { entriesApi.addEntryComment(body, embed, plus18, entryId) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())

    override fun editEntry(body: String, entryId: Int) = rxSingle { entriesApi.editEntry(body, entryId) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun markFavorite(entryId: Int) = rxSingle { entriesApi.markFavorite(entryId) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun deleteEntry(entryId: Int) = rxSingle { entriesApi.deleteEntry(entryId) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun editEntryComment(body: String, commentId: Int) = rxSingle { entriesApi.editEntryComment(body, commentId) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun deleteEntryComment(commentId: Int) = rxSingle { entriesApi.deleteEntryComment(commentId) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun voteSurvey(entryId: Int, answerId: Int) = rxSingle { entriesApi.voteSurvey(entryId, answerId) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .map { SurveyMapper.map(it) }

    override fun getHot(page: Int, period: String) = rxSingle { entriesApi.getHot(page, period) }
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> EntryMapper.map(response, owmContentFilter) } }

    override fun getStream(page: Int) = rxSingle { entriesApi.getStream(page) }
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> EntryMapper.map(response, owmContentFilter) } }

    override fun getActive(page: Int) = rxSingle { entriesApi.getActive(page) }
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> EntryMapper.map(response, owmContentFilter) } }

    override fun getObserved(page: Int) = rxSingle { entriesApi.getObserved(page) }
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> EntryMapper.map(response, owmContentFilter) } }

    override fun getEntry(id: Int) = rxSingle { entriesApi.getEntry(id) }
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose(ErrorHandlerTransformer())
        .map { EntryMapper.map(it, owmContentFilter) }

    override fun getEntryVoters(id: Int) = rxSingle { entriesApi.getEntryVoters(id) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> VoterMapper.map(response) } }

    override fun getEntryCommentVoters(id: Int) = rxSingle { entriesApi.getCommentUpvoters(id) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> VoterMapper.map(response) } }
}
