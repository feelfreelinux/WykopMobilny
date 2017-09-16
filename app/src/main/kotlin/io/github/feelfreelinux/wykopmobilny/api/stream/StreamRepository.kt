package io.github.feelfreelinux.wykopmobilny.api.stream

import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.userSessionToken
import retrofit2.Call
import retrofit2.Retrofit

interface StreamApi {
    fun getMikroblogIndex(page : Int): Call<List<Entry>>
    fun getMirkoblogHot(page : Int, period : Int): Call<List<Entry>>
}

class StreamRepository(val retrofit: Retrofit, private val apiPreferences: CredentialsPreferencesApi) : StreamApi {
    private val streamApi by lazy { retrofit.create(StreamRetrofitApi::class.java) }

    override fun getMikroblogIndex(page : Int) = streamApi.getMikroblogIndex(page, apiPreferences.userSessionToken)

    override fun getMirkoblogHot(page : Int, period : Int) = streamApi.getMikroblogHot(page, period, apiPreferences.userSessionToken)
}