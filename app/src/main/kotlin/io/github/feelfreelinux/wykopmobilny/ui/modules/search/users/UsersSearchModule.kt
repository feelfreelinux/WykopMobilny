package io.github.feelfreelinux.wykopmobilny.ui.modules.search.users

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class UsersSearchModule {
    @Provides
    fun provideUsersSearchPresenter(schedulers: Schedulers, searchApi: SearchApi) = UsersSearchPresenter(schedulers, searchApi)
}