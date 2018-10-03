package io.github.feelfreelinux.wykopmobilny.ui.modules.profile

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObserveStateResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ProfileResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.BadgeResponse

interface ProfileView : BaseView {
    fun showProfile(profileResponse: ProfileResponse)
    fun showButtons(observeState: ObserveStateResponse)
    fun showBadges(badges: List<BadgeResponse>)
}