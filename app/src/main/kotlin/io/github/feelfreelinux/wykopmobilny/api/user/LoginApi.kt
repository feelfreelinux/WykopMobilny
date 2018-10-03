package io.github.feelfreelinux.wykopmobilny.api.user

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LoginResponse
import io.reactivex.Single

interface LoginApi {
    fun getUserSessionToken(): Single<LoginResponse>
}