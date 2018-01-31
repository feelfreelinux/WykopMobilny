package io.github.feelfreelinux.wykopmobilny.api.profile

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ProfileResponse
import io.reactivex.Single

interface ProfileApi {
    fun getIndex(username: String) : Single<ProfileResponse>
}