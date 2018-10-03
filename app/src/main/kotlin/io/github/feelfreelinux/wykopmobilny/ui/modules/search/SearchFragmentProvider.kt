package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry.EntrySearchFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry.EntrySearchFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.links.LinkSearchFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.links.LinkSearchModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.users.UsersSearchFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.users.UsersSearchModule

@Module
abstract class SearchFragmentProvider {
    @ContributesAndroidInjector(modules = [EntrySearchFragmentModule::class])
    abstract fun provideEntryFragment(): EntrySearchFragment

    @ContributesAndroidInjector(modules = [LinkSearchModule::class])
    abstract fun provideLinksFrament(): LinkSearchFragment

    @ContributesAndroidInjector(modules = [UsersSearchModule::class])
    abstract fun provideUsersFragment(): UsersSearchFragment
}