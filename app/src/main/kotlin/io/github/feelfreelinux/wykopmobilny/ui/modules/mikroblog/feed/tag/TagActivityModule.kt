package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@Module
class TagActivityModule {
    @Provides
    fun provideNavigator(activity : TagActivity) : NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideLinkHandler(activity : TagActivity, navigator : NewNavigatorApi) : WykopLinkHandlerApi = WykopLinkHandler(activity, navigator)
}