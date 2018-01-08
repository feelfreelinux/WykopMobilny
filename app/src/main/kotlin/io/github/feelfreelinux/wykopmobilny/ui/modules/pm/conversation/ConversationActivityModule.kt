package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@Module
class ConversationActivityModule {
    @Provides
    fun provideConversationActivityPresenter(schedulers: Schedulers, pmApi: PMApi) = ConversationPresenter(schedulers, pmApi)

    @Provides
    fun provideNavigatorApi(activity : ConversationActivity) : NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideLinkHandlerApi(navigatorApi: NewNavigatorApi, activity: ConversationActivity) : WykopLinkHandlerApi = WykopLinkHandler(activity, navigatorApi)
}