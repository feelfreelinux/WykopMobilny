package io.github.wykopmobilny.api.patrons

import io.github.wykopmobilny.base.WykopSchedulers
import io.github.wykopmobilny.models.pojo.apiv2.patrons.Patron
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatronsRepository @Inject constructor(
    private val patronsApi: PatronsRetrofitApi
) : PatronsApi {

    override lateinit var patrons: List<Patron>
    override fun <T : Any> ensurePatrons(d: T): Single<T> {
        return Single.create { emitter ->
            if (!::patrons.isInitialized) {
                getPatrons().subscribeOn(WykopSchedulers().backgroundThread()).observeOn(WykopSchedulers().mainThread())
                    .subscribe(
                        { patrons ->
                            this.patrons = patrons
                            emitter.onSuccess(d)
                        },
                        { _ ->
                            this.patrons = listOf()
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
                this.patrons = it
            }.doOnError {
                this.patrons = listOf()
            }
}
