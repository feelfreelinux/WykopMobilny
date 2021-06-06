package io.github.wykopmobilny.ui.modules.blacklist

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BlacklistFragmentProvider {
    @ContributesAndroidInjector(modules = [])
    abstract fun bindTagsFragment(): BlacklistTagsFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun bindUsersFragment(): BlacklistUsersFragment
}
