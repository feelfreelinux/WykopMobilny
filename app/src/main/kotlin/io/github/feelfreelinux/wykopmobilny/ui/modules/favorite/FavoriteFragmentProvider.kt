package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.entry.EntryFavoriteFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.entry.EntryFavoriteFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.links.LinksFavoriteFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.links.LinksFavoriteFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index.MyWykopIndexFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index.MyWykopIndexFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.tags.MyWykopTagsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.tags.MyWykopTagsFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users.MyWykopUsersFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users.MyWykopUsersFragmentModule

@Module
abstract class FavoriteFragmentProvider {
    @ContributesAndroidInjector(modules = [LinksFavoriteFragmentModule::class])
    abstract fun provideLinksFavoriteFragment() : LinksFavoriteFragment

    @ContributesAndroidInjector(modules = [EntryFavoriteFragmentModule::class])
    abstract fun provideEntryFavoriteFragment() : EntryFavoriteFragment
}