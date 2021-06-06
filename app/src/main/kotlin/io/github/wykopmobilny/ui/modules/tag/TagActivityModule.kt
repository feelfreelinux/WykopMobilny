package io.github.wykopmobilny.ui.modules.tag

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.tag.TagApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

@Module
class TagActivityModule {

    @Provides
    fun providePresenter(schedulers: Schedulers, tagApi: TagApi) = TagActivityPresenter(schedulers, tagApi)

    @Provides
    fun provideNavigator(activity: TagActivity): NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideLinkHandler(activity: TagActivity, navigator: NewNavigatorApi): WykopLinkHandlerApi = WykopLinkHandler(activity, navigator)
}
