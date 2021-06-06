package io.github.wykopmobilny.ui.modules.addlink.fragments.confirmdetails

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.addlink.AddLinkApi
import io.github.wykopmobilny.base.Schedulers

@Module
class AddLinkDetailsFragmentModule {
    @Provides
    fun providesPresenter(schedulers: Schedulers, addLinkApi: AddLinkApi) =
        AddLinkDetailsFragmentPresenter(schedulers, addLinkApi)
}
