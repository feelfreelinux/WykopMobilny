package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.burried

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class BurriedLinksPresenter(val schedulers: Schedulers, profileApi: ProfileApi) : BasePresenter<BurriedLinksView>()