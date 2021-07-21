package io.github.wykopmobilny.api.patrons

import io.github.wykopmobilny.patrons.api.Patron
import io.github.wykopmobilny.patrons.api.PatronsRetrofitApi
import io.reactivex.Single
import kotlinx.coroutines.rx2.rxSingle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatronsRepository @Inject constructor(
    private val patronsApi: PatronsRetrofitApi,
) : PatronsApi {

    private var _patrons: List<Patron>? = null
    override val patrons: List<Patron>
        get() = _patrons.orEmpty()

    override fun <T : Any> ensurePatrons(continuation: T): Single<T> {
        return rxSingle {
            if (_patrons == null) {
                getPatronsOrEmpty()
            }
            continuation
        }
    }

    private suspend fun getPatronsOrEmpty() =
        runCatching { patronsApi.getPatrons().patrons }
            .getOrDefault(emptyList())
            .also { _patrons = it }
}
