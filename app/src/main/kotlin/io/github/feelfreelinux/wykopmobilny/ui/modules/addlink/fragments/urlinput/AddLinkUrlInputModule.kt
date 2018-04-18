package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.urlinput

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.addlink.AddlinkApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class AddLinkUrlInputModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, addlinkApi: AddlinkApi) = AddLinkUrlInputPresenter(schedulers, addlinkApi)
}