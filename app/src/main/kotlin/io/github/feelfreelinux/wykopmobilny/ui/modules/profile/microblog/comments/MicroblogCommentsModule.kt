package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.comments

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class MicroblogCommentsModule {
    @Provides
    fun providesPresenter(schedulers: Schedulers, profileApi: ProfileApi) = MicroblogCommentsPresenter(schedulers, profileApi)
}