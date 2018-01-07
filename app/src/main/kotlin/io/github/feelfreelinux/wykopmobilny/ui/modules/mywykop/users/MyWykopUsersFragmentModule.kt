package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class MyWykopUsersFragmentModule {
    @Provides
    fun provideMyWykopTagsFragmentPresenter(schedulers: Schedulers, myWykopApi: MyWykopApi) =
            MyWykopUsersPresenter(schedulers, myWykopApi)
}