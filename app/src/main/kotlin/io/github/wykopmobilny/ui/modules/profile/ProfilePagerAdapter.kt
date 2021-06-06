package io.github.wykopmobilny.ui.modules.profile

import android.content.res.Resources
import android.util.SparseArray
import android.view.ViewGroup
import io.github.wykopmobilny.R
import io.github.wykopmobilny.ui.modules.profile.actions.ActionsFragment
import io.github.wykopmobilny.ui.modules.profile.links.LinksFragment
import io.github.wykopmobilny.ui.modules.profile.microblog.MicroblogFragment

class ProfilePagerAdapter(
    val resources: Resources,
    fragmentManager: androidx.fragment.app.FragmentManager
) : androidx.fragment.app.FragmentPagerAdapter(fragmentManager) {

    val registeredFragments = SparseArray<androidx.fragment.app.Fragment>()

    override fun getItem(position: Int): androidx.fragment.app.Fragment = when (position) {
        0 -> ActionsFragment.newInstance()
        1 -> LinksFragment.newInstance()
        else -> MicroblogFragment.newInstance()
    }

    override fun getCount() = 3

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as androidx.fragment.app.Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.removeAt(position)
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence {
        super.getPageTitle(position)
        return when (position) {
            0 -> resources.getString(R.string.actions)
            1 -> resources.getString(R.string.links)
            else -> resources.getString(R.string.mikroblog)
        }
    }
}
