package io.github.wykopmobilny.ui.modules.search.users

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.search.SearchApi
import io.github.wykopmobilny.base.Schedulers

@Module
class UsersSearchModule {
    @Provides
    fun provideUsersSearchPresenter(schedulers: Schedulers, searchApi: SearchApi) = UsersSearchPresenter(schedulers, searchApi)
}
