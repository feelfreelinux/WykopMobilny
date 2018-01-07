package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class MyWykopIndexFragmentModule {
    @Provides
    fun provideMyWykopIndexFragmentPresenter(schedulers: Schedulers, myWykopApi: MyWykopApi) =
            MyWykopIndexPresenter(schedulers, myWykopApi)
}