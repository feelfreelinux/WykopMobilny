package io.github.feelfreelinux.wykopmobilny.api

import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function
import org.reactivestreams.Publisher

class UserTokenRefresher(private val userApi: UserApi, private val userManagerApi: UserManagerApi) : Function<Flowable<Throwable>, Publisher<*>> {
    override fun apply(t: Flowable<Throwable>): Publisher<*> {
        return t.flatMap {
            if (it is WykopRequestBodyConverterFactory.ApiException) {
                if (it.code == 11 || it.code == 12) {
                    userApi.getUserSessionToken().flatMap {
                            userManagerApi.saveCredentials(it)
                            Single.just(it)
                    }.toFlowable()
                } else Flowable.error(it)
            } else Flowable.error(it)
        }
    }
}