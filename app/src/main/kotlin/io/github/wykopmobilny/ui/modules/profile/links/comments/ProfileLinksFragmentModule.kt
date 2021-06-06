package io.github.wykopmobilny.ui.modules.profile.links.comments

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.profile.ProfileApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.linkcomments.LinkCommentInteractor

@Module
class ProfileLinksFragmentModule {
    @Provides
    fun providePresenter(
        schedulers: Schedulers,
        profileApi: ProfileApi,
        linksInteractor: LinkCommentInteractor
    ) = ProfileLinksFragmentPresenter(schedulers, profileApi, linksInteractor)
}
