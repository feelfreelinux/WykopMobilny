package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.digged

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class DiggedLinksPresenter(val schedulers: Schedulers, val profileApi: ProfileApi) : BasePresenter<DiggedLinksView>()