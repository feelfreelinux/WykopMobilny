package io.github.wykopmobilny.api.user

import io.github.wykopmobilny.api.endpoints.LoginRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.storage.api.SessionStorage
import kotlinx.coroutines.rx2.rxSingle
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val tagApi: LoginRetrofitApi,
    private val apiPreferences: SessionStorage,
) : LoginApi {

    override fun getUserSessionToken() =
        rxSingle {
            val session = apiPreferences.session.value.let(::checkNotNull)
            tagApi.getUserSessionToken(login = session.login, accountKey = session.token)
        }
            .compose(ErrorHandlerTransformer())
}
