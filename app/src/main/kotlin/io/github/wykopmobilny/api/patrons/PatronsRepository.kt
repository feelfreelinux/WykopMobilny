package io.github.wykopmobilny.api.patrons

import io.github.wykopmobilny.base.WykopSchedulers
import io.github.wykopmobilny.patrons.api.Patron
import io.github.wykopmobilny.patrons.api.PatronsRetrofitApi
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatronsRepository @Inject constructor(
    private val patronsApi: PatronsRetrofitApi
) : PatronsApi {

    private lateinit var _patrons: List<Patron>
    override val patrons: List<Patron>
        get() = if (::_patrons.isInitialized) {
            _patrons
        } else {
            emptyList()
        }

    override fun <T : Any> ensurePatrons(d: T): Single<T> {
        return Single.create { emitter ->
            if (!::_patrons.isInitialized) {
                getPatrons().subscribeOn(WykopSchedulers().backgroundThread()).observeOn(WykopSchedulers().mainThread())
                    .subscribe(
                        { patrons ->
                            this._patrons = patrons
                            emitter.onSuccess(d)
                        },
                        { _ ->
                            this._patrons = listOf()
                            emitter.onSuccess(d)
                        }
                    )
            } else {
                emitter.onSuccess(d)
            }
        }
    }

    override fun getPatrons(): Single<List<Patron>> =
        patronsApi.getPatrons().map { it.patrons }
            .doOnSuccess {
                this._patrons = it
            }.doOnError {
                this._patrons = listOf()
            }
}
