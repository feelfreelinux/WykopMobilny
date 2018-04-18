package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.confirmdetails

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.addlink.AddlinkApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class AddLinkDetailsFragmentModule {
    @Provides
    fun providesPresenter(schedulers: Schedulers, addlinkApi: AddlinkApi) = AddLinkDetailsFragmentPresenter(schedulers, addlinkApi)
}