package io.github.feelfreelinux.wykopmobilny.api.patrons

import io.github.feelfreelinux.wykopmobilny.base.WykopSchedulers
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.patrons.Patron
import io.reactivex.Single
import retrofit2.Retrofit

class PatronsRepository(val retrofit: Retrofit) : PatronsApi {
    private val patronsApi by lazy { retrofit.create(PatronsRetrofitApi::class.java) }

    override lateinit var patrons: List<Patron>
    override fun <T : Any> ensurePatrons(d: T): Single<T> {
        return Single.create {
            emitter ->
            if (!::patrons.isInitialized) {
                getPatrons().subscribeOn(WykopSchedulers().backgroundThread()).observeOn(WykopSchedulers().mainThread())
                    .subscribe(
                        {
                            patrons ->
                            this.patrons = patrons
                            emitter.onSuccess(d)
                        },
                        { e ->
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
