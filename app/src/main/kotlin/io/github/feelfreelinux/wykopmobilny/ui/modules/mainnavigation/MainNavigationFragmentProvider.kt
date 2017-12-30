package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted.PromotedFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted.PromotedFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragment

@Module
abstract class MainNavigationFragmentProvider {
    @ContributesAndroidInjector(modules = [PromotedFragmentModule::class])
    abstract fun providePromotedFragment() : PromotedFragment

    @ContributesAndroidInjector(modules = [PromotedFragmentModule::class])
    abstract fun provideHotFragment() : HotFragment
}