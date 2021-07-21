package io.github.wykopmobilny.api.user

import io.github.wykopmobilny.api.endpoints.LoginRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.storage.api.SessionStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.rx2.rxSingle
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginApi: LoginRetrofitApi,
    private val apiPreferences: SessionStorage,
) : LoginApi {

    override fun getUserSessionToken() =
        rxSingle {
            val session = apiPreferences.session.first().let(::checkNotNull)
            loginApi.getUserSessionToken(login = session.login, accountKey = session.token)
        }
            .compose(ErrorHandlerTransformer())
}
