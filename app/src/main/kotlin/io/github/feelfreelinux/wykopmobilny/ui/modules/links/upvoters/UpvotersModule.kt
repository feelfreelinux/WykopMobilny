package io.github.feelfreelinux.wykopmobilny.ui.modules.links.upvoters

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@Module
class UpvotersModule {
    @Provides
    fun provideUpvotersPresenter(schedulers: Schedulers, linksApi: LinksApi) = UpvotersPresenter(schedulers, linksApi)

    @Provides
    fun provideNavigatorApi(activity: UpvotersActivity) : NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideWykopLinkHandler(activity: UpvotersActivity, navigatorApi: NewNavigatorApi) : WykopLinkHandlerApi
            = WykopLinkHandler(activity, navigatorApi)
}