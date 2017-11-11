package io.github.feelfreelinux.wykopmobilny.api

import io.github.feelfreelinux.wykopmobilny.api.errorhandler.WykopExceptionParser
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LoginResponse
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function
import org.reactivestreams.Publisher
import retrofit2.HttpException

class UserTokenRefresher(private val userApi: LoginApi, private val userManagerApi: UserManagerApi) : Function<Flowable<Throwable>, Publisher<*>> {
    override fun apply(t: Flowable<Throwable>): Publisher<*> {
        return t.flatMap {
            when (it) {
                is WykopExceptionParser.WykopApiException -> {
                    if (it.code == 11 || it.code == 12) {
                        getSaveUserSessionFlowable()
                    } else Flowable.error(it)
                }

                is HttpException -> {
                    if (it.code() == 401) {
                        getSaveUserSessionFlowable()
                    } else Flowable.error(it)
                }

                else -> Flowable.error(it)
            }
        }
    }

    fun getSaveUserSessionFlowable(): Flowable<LoginResponse> {
        return userApi.getUserSessionToken().flatMap {
            userManagerApi.saveCredentials(it)
            Single.just(it)
        }.toFlowable()
    }
}