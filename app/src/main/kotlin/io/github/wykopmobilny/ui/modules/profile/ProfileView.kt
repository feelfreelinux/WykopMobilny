package io.github.wykopmobilny.ui.modules.profile

import io.github.wykopmobilny.api.responses.BadgeResponse
import io.github.wykopmobilny.api.responses.ObserveStateResponse
import io.github.wykopmobilny.api.responses.ProfileResponse
import io.github.wykopmobilny.base.BaseView

interface ProfileView : BaseView {
    fun showProfile(profileResponse: ProfileResponse)
    fun showButtons(observeState: ObserveStateResponse)
    fun showBadges(badges: List<BadgeResponse>)
}
