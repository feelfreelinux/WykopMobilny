package io.github.wykopmobilny.ui.blacklist.android

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.github.wykopmobilny.ui.blacklist.android.page.BlacklistTagsFragment
import io.github.wykopmobilny.ui.blacklist.android.page.BlacklistUsersFragment

internal class BlacklistAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {

    override fun getItemCount() = 2

    fun getTitle(position: Int) =
        when (position) {
            0 -> R.string.blacklist_tab_title_tags
            1 -> R.string.blacklist_tab_title_users
            else -> error("unsupported")
        }

    override fun createFragment(position: Int) =
        when (position) {
            0 -> BlacklistTagsFragment()
            1 -> BlacklistUsersFragment()
            else -> error("unsupported")
        }
}
