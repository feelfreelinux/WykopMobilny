package io.github.feelfreelinux.wykopmobilny.api.stream

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.mapToEntry
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.userSessionToken
import io.reactivex.Single
import retrofit2.Retrofit

interface StreamApi {
    fun getMikroblogIndex(page : Int): Single<List<Entry>>
    fun getMirkoblogHot(page : Int, period : Int): Single<List<Entry>>
}

class StreamRepository(val retrofit: Retrofit) : StreamApi {
    private val streamApi by lazy { retrofit.create(StreamRetrofitApi::class.java) }

    override fun getMikroblogIndex(page : Int) = streamApi.getMikroblogIndex(page).map { it.map { it.mapToEntry() } }

    override fun getMirkoblogHot(page : Int, period : Int) = streamApi.getMikroblogHot(page, period).map { it.map { it.mapToEntry() } }
}