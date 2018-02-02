package io.github.feelfreelinux.wykopmobilny.ui.modules.profile

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ProfileResponse

interface ProfileView : BaseView {
    fun showProfile(profileResponse: ProfileResponse)
}