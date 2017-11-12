package io.github.feelfreelinux.wykopmobilny.api.entries

import io.github.feelfreelinux.wykopmobilny.api.*
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.EntryMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.SurveyMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.InputStream

data class TypedInputStream(val fileName : String, val mimeType : String, val inputStream: InputStream)

class EntriesRepository(val retrofit: Retrofit) : EntriesApi {
    private val entriesApi by lazy { retrofit.create(EntriesRetrofitApi::class.java) }

    override fun voteEntry(entryId: Int) = entriesApi.voteEntry(entryId)
            .compose<VoteResponse>(ErrorHandlerTransformer())

    override fun unvoteEntry(entryId: Int) = entriesApi.unvoteEntry(entryId)
            .compose<VoteResponse>(ErrorHandlerTransformer())


    override fun voteComment(commentId: Int) = entriesApi.voteComment(commentId)
            .compose<VoteResponse>(ErrorHandlerTransformer())


    override fun unvoteComment(commentId: Int) = entriesApi.unvoteComment(commentId)
            .compose<VoteResponse>(ErrorHandlerTransformer())


    override fun addEntry(body : String, inputStream: TypedInputStream) = entriesApi.addEntry(body.toRequestBody(), inputStream.getFileMultipart())
            .compose<EntryResponse>(ErrorHandlerTransformer())

    override fun addEntry(body : String, embed: String?) = entriesApi.addEntry(body, embed)
            .compose<EntryResponse>(ErrorHandlerTransformer())

    override fun addEntryComment(body : String, entryId: Int, inputStream: TypedInputStream) =
            entriesApi.addEntryComment(body.toRequestBody(), entryId, inputStream.getFileMultipart())
                    .compose<EntryCommentResponse>(ErrorHandlerTransformer())

    override fun addEntryComment(body : String, entryId: Int, embed: String?) = entriesApi.addEntryComment(body, embed, entryId)
            .compose<EntryCommentResponse>(ErrorHandlerTransformer())

    override fun editEntry(body : String, entryId: Int) = entriesApi.editEntry(body, entryId)
            .compose<EntryResponse>(ErrorHandlerTransformer())

    override fun markFavorite(entryId: Int) = entriesApi.markFavorite(entryId)
            .compose<FavoriteResponse>(ErrorHandlerTransformer())

    override fun deleteEntry(entryId: Int) = entriesApi.deleteEntry(entryId)
            .compose<EntryResponse>(ErrorHandlerTransformer())

    override fun editEntryComment(body: String, commentId: Int) = entriesApi.editEntryComment(body, commentId)
            .compose<EntryCommentResponse>(ErrorHandlerTransformer())

    override fun deleteEntryComment(commentId: Int) = entriesApi.deleteEntryComment(commentId)
            .compose<EntryCommentResponse>(ErrorHandlerTransformer())

    override fun voteSurvey(entryId: Int, answerId: Int) = entriesApi.voteSurvey(entryId, answerId)
            .compose<SurveyResponse>(ErrorHandlerTransformer())
            .map { SurveyMapper.map(it) }

    private fun TypedInputStream.getFileMultipart() =
            MultipartBody.Part.createFormData("embed", fileName, inputStream.getRequestBody(mimeType))!!

    private fun String.toRequestBody() = RequestBody.create(MultipartBody.FORM, this)!!

    override fun getHot(page : Int, period : String) = entriesApi.getHot(page, period)
            .compose<List<EntryResponse>>(ErrorHandlerTransformer())
            .map { it.map { EntryMapper.map(it) } }

    override fun getStream(page : Int) = entriesApi.getStream(page)
            .compose<List<EntryResponse>>(ErrorHandlerTransformer())
            .map { it.map { EntryMapper.map(it) } }

    override fun getEntry(id : Int) = entriesApi.getEntry(id)
            .compose<EntryResponse>(ErrorHandlerTransformer())
            .map { EntryMapper.map(it) }

}