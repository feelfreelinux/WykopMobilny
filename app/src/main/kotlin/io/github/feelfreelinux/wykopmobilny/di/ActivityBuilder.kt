package io.github.feelfreelinux.wykopmobilny.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationFragmentProvider
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryDetailModule


@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [EntryDetailModule::class])
    abstract fun bindEntryDetailsActivity(): EntryActivity

    @ContributesAndroidInjector(modules = [MainNavigationModule::class, MainNavigationFragmentProvider::class])
    abstract fun bindMainNavigationActivity()
}