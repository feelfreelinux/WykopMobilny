package io.github.feelfreelinux.wykopmobilny.api

import io.github.feelfreelinux.wykopmobilny.api.WykopRequestBodyConverterFactory
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.Function
import org.reactivestreams.Publisher

class UserTokenRefresher(private val userApi: UserApi, private val userManagerApi: UserManagerApi) : Function<Flowable<Throwable>, Publisher<*>> {
    override fun apply(t: Flowable<Throwable>): Publisher<*> {
        return t.flatMap {
            if (it is WykopRequestBodyConverterFactory.ApiException) {
                if (it.code == 11 || it.code == 12) {
                    try {
                        val profile = userApi.getUserSessionToken().blockingGet()
                        userManagerApi.saveCredentials(profile)
                        Flowable.just(it)
                    } catch (e : Throwable) { // Prevents error loop
                        if (e is WykopRequestBodyConverterFactory.ApiException && (e.code == 12 || e.code == 11)) {
                            val exception = WykopRequestBodyConverterFactory.ApiException(e.message, 0)
                            Flowable.error<WykopRequestBodyConverterFactory.ApiException>(exception)
                        } else Flowable.error(e)
                    }
                } else Flowable.error(it)
            } else Flowable.error(it)
        }
    }
}