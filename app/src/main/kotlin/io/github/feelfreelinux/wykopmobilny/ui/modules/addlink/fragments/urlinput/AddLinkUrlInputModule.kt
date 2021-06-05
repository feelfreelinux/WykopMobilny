package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.urlinput

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.addlink.AddLinkApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class AddLinkUrlInputModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, addLinkApi: AddLinkApi) = AddLinkUrlInputPresenter(schedulers, addLinkApi)
}
