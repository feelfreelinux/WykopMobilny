package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

@Module
class MainNavigationModule {
    @Provides
    fun provideMainNavigationPresenter(userManagerApi: UserManagerApi) = MainNavigationPresenter(userManagerApi)
}