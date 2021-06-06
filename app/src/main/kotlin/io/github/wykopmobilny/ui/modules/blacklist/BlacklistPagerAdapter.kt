package io.github.wykopmobilny.ui.modules.blacklist

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class BlacklistPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int) =
        if (position == 0) {
            BlacklistTagsFragment.createFragment()
        } else {
            BlacklistUsersFragment.createFragment()
        }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int) =
        if (position == 0) {
            "Tagi"
        } else {
            "Użytkownicy"
        }
}
