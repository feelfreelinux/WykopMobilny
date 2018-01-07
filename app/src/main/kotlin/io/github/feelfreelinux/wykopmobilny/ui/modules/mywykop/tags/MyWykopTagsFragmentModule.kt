package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.tags

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class MyWykopTagsFragmentModule {
    @Provides
    fun provideMyWykopTagsFragmentPresenter(schedulers: Schedulers, myWykopApi: MyWykopApi) =
            MyWykopTagsPresenter(schedulers, myWykopApi)
}