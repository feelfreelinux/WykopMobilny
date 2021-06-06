package io.github.wykopmobilny.ui.fragments.entries

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class EntriesFragmentProvider {
    @ContributesAndroidInjector(modules = [EntriesFragmentModule::class])
    abstract fun bindEntriesFragment(): EntriesFragment
}
