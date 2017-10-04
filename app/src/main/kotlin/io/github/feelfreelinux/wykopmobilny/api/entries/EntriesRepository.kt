package io.github.feelfreelinux.wykopmobilny.api.entries

import io.github.feelfreelinux.wykopmobilny.api.*
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.pojo.VoteResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.entries.FavoriteEntryResponse
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.userSessionToken
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.InputStream

interface EntriesApi {
    fun getEntryIndex(entryId : Int): Single<Entry>
    fun voteEntry(entryId: Int): Single<VoteResponse>
    fun unvoteEntry(entryId: Int): Single<VoteResponse>
    fun voteComment(entryId: Int, commentId: Int): Single<VoteResponse>
    fun unvoteComment(entryId: Int, commentId: Int): Single<VoteResponse>
    fun addEntry(body : String, inputStream: TypedInputStream): Single<AddResponse>
    fun addEntry(body: String, embed: String?): Single<AddResponse>
    fun addEntryComment(body: String, entryId: Int, embed: String?): Single<AddResponse>
    fun addEntryComment(body: String, entryId: Int, inputStream: TypedInputStream): Single<AddResponse>
    fun markFavorite(entryId: Int) : Single<FavoriteEntryResponse>
}

data class TypedInputStream(val fileName : String, val mimeType : String, val inputStream: InputStream)

class EntriesRepository(val retrofit: Retrofit, private val apiPreferences: CredentialsPreferencesApi) : EntriesApi {
    private val entriesApi by lazy { retrofit.create(EntriesRetrofitApi::class.java) }

    override fun getEntryIndex(entryId : Int) = entriesApi.getEntryIndex(entryId, apiPreferences.userSessionToken).map { it.mapToEntry() }

    override fun voteEntry(entryId: Int) = entriesApi.voteEntry(entryId, apiPreferences.userSessionToken)

    override fun unvoteEntry(entryId: Int) = entriesApi.unvoteEntry(entryId, apiPreferences.userSessionToken)

    override fun voteComment(entryId: Int, commentId: Int) = entriesApi.voteComment(entryId, commentId, apiPreferences.userSessionToken)

    override fun unvoteComment(entryId: Int, commentId: Int) = entriesApi.unvoteComment(entryId, commentId, apiPreferences.userSessionToken)

    override fun addEntry(body : String, inputStream: TypedInputStream) =
        entriesApi.addEntry(body.toRequestBody(), apiPreferences.userSessionToken, inputStream.getFileMultipart())

    override fun addEntry(body : String, embed: String?) =
            entriesApi.addEntry(body, embed, apiPreferences.userSessionToken)

    override fun addEntryComment(body : String, entryId: Int, inputStream: TypedInputStream) =
            entriesApi.addEntryComment(body.toRequestBody(), entryId, apiPreferences.userSessionToken, inputStream.getFileMultipart())

    override fun addEntryComment(body : String, entryId: Int, embed: String?) =
            entriesApi.addEntryComment(body, embed, entryId, apiPreferences.userSessionToken)

    override fun markFavorite(entryId: Int) =
            entriesApi.markFavorite(entryId, apiPreferences.userSessionToken)

    private fun TypedInputStream.getFileMultipart() =
            MultipartBody.Part.createFormData("embed", fileName, inputStream.getRequestBody(mimeType))!!

    private fun String.toRequestBody() =
            RequestBody.create(MultipartBody.FORM, this)!!
}