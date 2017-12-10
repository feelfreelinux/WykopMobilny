package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index.MyWykopIndexFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.tags.MyWykopTagsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users.MyWykopUsersFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry.EntrySearchFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.links.LinkSearchFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.users.UsersSearchFragment
import io.github.feelfreelinux.wykopmobilny.utils.printout

class SearchPagerAdapter(val resources : Resources, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> LinkSearchFragment.newInstance()
            1 -> EntrySearchFragment.newInstance()
            else -> UsersSearchFragment.newInstance()
        }
    }

    override fun getCount() = 3

    override fun getPageTitle(position: Int): CharSequence {
        super.getPageTitle(position)
        return when(position) {
            0 -> resources.getString(R.string.links)
            1 -> resources.getString(R.string.entries)
            else -> resources.getString(R.string.profiles)
        }
    }

    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }
}