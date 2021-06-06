package io.github.wykopmobilny.api.user

import io.github.wykopmobilny.api.responses.LoginResponse
import io.reactivex.Single

interface LoginApi {
    fun getUserSessionToken(): Single<LoginResponse>
}
