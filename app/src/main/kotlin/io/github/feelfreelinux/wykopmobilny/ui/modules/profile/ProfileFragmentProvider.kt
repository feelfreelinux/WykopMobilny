package io.github.feelfreelinux.wykopmobilny.ui.modules.profile

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.actions.ActionsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.actions.ActionsFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.LinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.LinksFragmentProvider
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.MicroblogFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.MicroblogFragmentProvider

@Module
abstract class ProfileFragmentProvider {

    @ContributesAndroidInjector(modules = [ActionsFragmentModule::class])
    abstract fun provideActionsFragment(): ActionsFragment

    @ContributesAndroidInjector(modules = [LinksFragmentProvider::class])
    abstract fun provideLinksFragment(): LinksFragment

    @ContributesAndroidInjector(modules = [MicroblogFragmentProvider::class])
    abstract fun provideMicroblogFragment(): MicroblogFragment
}