package io.github.wykopmobilny.ui.modules.addlink.fragments.urlinput

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.addlink.AddLinkApi
import io.github.wykopmobilny.base.Schedulers

@Module
class AddLinkUrlInputModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, addLinkApi: AddLinkApi) = AddLinkUrlInputPresenter(schedulers, addLinkApi)
}
