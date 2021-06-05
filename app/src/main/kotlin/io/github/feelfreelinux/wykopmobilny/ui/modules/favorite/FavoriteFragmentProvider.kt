package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.entry.EntryFavoriteFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.entry.EntryFavoriteFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.links.LinksFavoriteFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.links.LinksFavoriteFragmentModule

@Module
abstract class FavoriteFragmentProvider {
    @ContributesAndroidInjector(modules = [LinksFavoriteFragmentModule::class])
    abstract fun provideLinksFavoriteFragment(): LinksFavoriteFragment

    @ContributesAndroidInjector(modules = [EntryFavoriteFragmentModule::class])
    abstract fun provideEntryFavoriteFragment(): EntryFavoriteFragment
}
