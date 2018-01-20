package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.entry.EntryFavoriteFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.links.LinksFavoriteFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index.MyWykopIndexFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.tags.MyWykopTagsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users.MyWykopUsersFragment

class FavoritePagerAdapter(val resources : Resources, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment = when(position) {
        0 -> LinksFavoriteFragment.newInstance()
        else -> EntryFavoriteFragment.newInstance()
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence {
        super.getPageTitle(position)
        return when(position) {
            0 -> resources.getString(R.string.links)
            else -> resources.getString(R.string.entries)
        }
    }
}