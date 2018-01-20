package io.github.feelfreelinux.wykopmobilny.api.entries

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.api.getRequestBody
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.EntryMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.SurveyMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.VoterMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.InputStream

data class TypedInputStream(val fileName : String, val mimeType : String, val inputStream: InputStream)

class EntriesRepository(val retrofit: Retrofit, val userTokenRefresher: UserTokenRefresher) : EntriesApi {
    private val entriesApi by lazy { retrofit.create(EntriesRetrofitApi::class.java) }

    override fun voteEntry(entryId: Int) = entriesApi.voteEntry(entryId)
            .retryWhen(userTokenRefresher)
            .compose<VoteResponse>(ErrorHandlerTransformer())

    override fun unvoteEntry(entryId: Int) = entriesApi.unvoteEntry(entryId)
            .retryWhen(userTokenRefresher)
            .compose<VoteResponse>(ErrorHandlerTransformer())


    override fun voteComment(commentId: Int) = entriesApi.voteComment(commentId)
            .retryWhen(userTokenRefresher)
            .compose<VoteResponse>(ErrorHandlerTransformer())


    override fun unvoteComment(commentId: Int) = entriesApi.unvoteComment(commentId)
            .retryWhen(userTokenRefresher)
            .compose<VoteResponse>(ErrorHandlerTransformer())


    override fun addEntry(body : String, inputStream: TypedInputStream, plus18: Boolean) =
            entriesApi.addEntry(body.toRequestBody(), plus18.toRequestBody(), inputStream.getFileMultipart())
            .retryWhen(userTokenRefresher)
            .compose<EntryResponse>(ErrorHandlerTransformer())

    override fun addEntry(body : String, embed: String?, plus18 : Boolean) = entriesApi.addEntry(body, embed, plus18)
            .retryWhen(userTokenRefresher)
            .compose<EntryResponse>(ErrorHandlerTransformer())

    override fun addEntryComment(body : String, entryId: Int, inputStream: TypedInputStream, plus18: Boolean) =
            entriesApi.addEntryComment(body.toRequestBody(), plus18.toRequestBody(), entryId, inputStream.getFileMultipart())
                    .retryWhen(userTokenRefresher)
                    .compose<EntryCommentResponse>(ErrorHandlerTransformer())

    override fun addEntryComment(body : String, entryId: Int, embed: String?, plus18: Boolean) =
            entriesApi.addEntryComment(body, embed, plus18, entryId)
            .retryWhen(userTokenRefresher)
            .compose<EntryCommentResponse>(ErrorHandlerTransformer())

    override fun editEntry(body : String, entryId: Int) = entriesApi.editEntry(body, entryId)
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

    private fun TypedInputStream.getFileMultipart() =
            MultipartBody.Part.createFormData("embed", fileName, inputStream.getRequestBody(mimeType))!!

    private fun String.toRequestBody() = RequestBody.create(MultipartBody.FORM, this)!!
    private fun Boolean.toRequestBody() = RequestBody.create(MultipartBody.FORM, if (this) "1" else "")!!

    override fun getHot(page : Int, period : String) = entriesApi.getHot(page, period)
            .retryWhen(userTokenRefresher)
            .compose<List<EntryResponse>>(ErrorHandlerTransformer())
            .map { it.map { EntryMapper.map(it) } }

    override fun getStream(page : Int) = entriesApi.getStream(page)
            .retryWhen(userTokenRefresher)
            .compose<List<EntryResponse>>(ErrorHandlerTransformer())
            .map { it.map { EntryMapper.map(it) } }

    override fun getObserved(page : Int) = entriesApi.getObserved(page)
            .retryWhen(userTokenRefresher)
            .compose<List<EntryResponse>>(ErrorHandlerTransformer())
            .map { it.map { EntryMapper.map(it) } }

    override fun getEntry(id : Int) = entriesApi.getEntry(id)
            .retryWhen(userTokenRefresher)
            .compose<EntryResponse>(ErrorHandlerTransformer())
            .map { EntryMapper.map(it) }

    override fun getEntryVoters(id: Int) = entriesApi.getEntryVoters(id)
            .retryWhen(userTokenRefresher)
            .compose<List<VoterResponse>>(ErrorHandlerTransformer())
            .map { it.map { VoterMapper.map(it) } }

    override fun getEntryCommentVoters(id: Int) = entriesApi.getCommentUpvoters(id)
            .retryWhen(userTokenRefresher)
            .compose<List<VoterResponse>>(ErrorHandlerTransformer())
            .map { it.map { VoterMapper.map(it) } }
}