package io.github.feelfreelinux.wykopmobilny.api.entries

import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.api.VoteResponse
import io.github.feelfreelinux.wykopmobilny.api.getRequestBody
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.userSessionToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import java.io.InputStream

interface EntriesApi {
    fun getEntryIndex(entryId : Int): Call<Entry>
    fun voteEntry(entryId: Int): Call<VoteResponse>
    fun unvoteEntry(entryId: Int): Call<VoteResponse>
    fun voteComment(entryId: Int, commentId: Int): Call<VoteResponse>
    fun unvoteComment(entryId: Int, commentId: Int): Call<VoteResponse>
    fun addEntry(body : String, inputStream: TypedInputStream): Call<AddResponse>
    fun addEntry(body: String, embed: String): Call<AddResponse>
    fun addEntryComment(body: String, entryId: Int, embed: String): Call<AddResponse>
    fun addEntryComment(body: String, entryId: Int, inputStream: TypedInputStream): Call<AddResponse>
}

data class TypedInputStream(val fileName : String, val mimeType : String, val inputStream: InputStream)

class EntriesRepository(val retrofit: Retrofit, private val apiPreferences: CredentialsPreferencesApi) : EntriesApi {
    private val entriesApi by lazy { retrofit.create(EntriesRetrofitApi::class.java) }

    override fun getEntryIndex(entryId : Int) = entriesApi.getEntryIndex(entryId, apiPreferences.userSessionToken)

    override fun voteEntry(entryId: Int) = entriesApi.voteEntry(entryId, apiPreferences.userSessionToken)

    override fun unvoteEntry(entryId: Int) = entriesApi.unvoteEntry(entryId, apiPreferences.userSessionToken)

    override fun voteComment(entryId: Int, commentId: Int) = entriesApi.voteComment(entryId, commentId, apiPreferences.userSessionToken)

    override fun unvoteComment(entryId: Int, commentId: Int) = entriesApi.unvoteComment(entryId, commentId, apiPreferences.userSessionToken)

    override fun addEntry(body : String, inputStream: TypedInputStream): Call<AddResponse> =
        entriesApi.addEntry(body.toRequestBody(), apiPreferences.userSessionToken, inputStream.getFileMultipart())

    override fun addEntry(body : String, embed: String): Call<AddResponse> =
            entriesApi.addEntry(body, embed, apiPreferences.userSessionToken)

    override fun addEntryComment(body : String, entryId: Int, inputStream: TypedInputStream): Call<AddResponse> =
            entriesApi.addEntryComment(body.toRequestBody(), entryId, apiPreferences.userSessionToken, inputStream.getFileMultipart())

    override fun addEntryComment(body : String, entryId: Int, embed: String): Call<AddResponse> =
            entriesApi.addEntryComment(body, embed, entryId, apiPreferences.userSessionToken)

    private fun TypedInputStream.getFileMultipart() =
            MultipartBody.Part.createFormData("embed", fileName, inputStream.getRequestBody(mimeType))!!

    private fun String.toRequestBody() =
            RequestBody.create(MultipartBody.FORM, this)!!
}