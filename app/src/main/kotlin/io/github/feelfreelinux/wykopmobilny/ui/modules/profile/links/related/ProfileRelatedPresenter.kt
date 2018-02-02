package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.related

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class ProfileRelatedPresenter(val schedulers: Schedulers, val profileApi: ProfileApi) : BasePresenter<ProfileRelatedView>()