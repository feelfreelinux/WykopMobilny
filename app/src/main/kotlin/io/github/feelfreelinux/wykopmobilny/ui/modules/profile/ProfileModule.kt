package io.github.feelfreelinux.wykopmobilny.ui.modules.profile

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@Module
class ProfileModule {

    @Provides
    fun providePresenter(schedulers: Schedulers, profilesApi: ProfileApi) = ProfilePresenter(schedulers, profilesApi)

    @Provides
    fun provideNavigator(activity: ProfileActivity): NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideLinkHandler(activity: ProfileActivity, navigator: NewNavigatorApi): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigator)
}
