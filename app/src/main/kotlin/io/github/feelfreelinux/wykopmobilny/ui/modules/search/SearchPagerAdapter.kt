package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry.EntrySearchFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.links.LinkSearchFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.users.UsersSearchFragment

class SearchPagerAdapter(val resources : Resources, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    val registeredFragments = SparseArray<Fragment>()

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> LinkSearchFragment.newInstance()
            1 -> EntrySearchFragment.newInstance()
            else -> UsersSearchFragment.newInstance()
        }
    }

    override fun getCount() = 3

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.removeAt(position)
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence {
        super.getPageTitle(position)
        return when(position) {
            0 -> resources.getString(R.string.links)
            1 -> resources.getString(R.string.entries)
            else -> resources.getString(R.string.profiles)
        }
    }


}