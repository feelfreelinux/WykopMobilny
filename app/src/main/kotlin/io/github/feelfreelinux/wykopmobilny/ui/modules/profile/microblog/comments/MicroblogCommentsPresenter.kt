package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.comments

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class MicroblogCommentsPresenter(val schedulers: Schedulers, profileApi: ProfileApi) : BasePresenter<MicroblogCommentsView>()