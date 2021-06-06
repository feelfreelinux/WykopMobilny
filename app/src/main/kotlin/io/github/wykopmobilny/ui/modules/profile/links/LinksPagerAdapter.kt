package io.github.wykopmobilny.ui.modules.profile.links

import android.content.res.Resources
import android.util.SparseArray
import android.view.ViewGroup
import io.github.wykopmobilny.R
import io.github.wykopmobilny.ui.modules.profile.links.added.ProfileLinksFragment
import io.github.wykopmobilny.ui.modules.profile.links.comments.ProfileLinkCommentsFragment
import io.github.wykopmobilny.ui.modules.profile.links.related.ProfileRelatedFragment

class LinksPagerAdapter(
    val resources: Resources,
    fragmentManager: androidx.fragment.app.FragmentManager
) : androidx.fragment.app.FragmentPagerAdapter(fragmentManager) {

    val registeredFragments = SparseArray<androidx.fragment.app.Fragment>()

    override fun getItem(position: Int): androidx.fragment.app.Fragment = when (position) {
        0 -> ProfileLinksFragment.newInstance(ProfileLinksFragment.TYPE_ADDED)
        1 -> ProfileLinksFragment.newInstance(ProfileLinksFragment.TYPE_PUBLISHED)
        2 -> ProfileLinkCommentsFragment.newInstance()
        3 -> ProfileRelatedFragment.newInstance()
        4 -> ProfileLinksFragment.newInstance(ProfileLinksFragment.TYPE_DIGGED)
        else -> ProfileLinksFragment.newInstance(ProfileLinksFragment.TYPE_BURRIED)
    }

    override fun getCount() = 6

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
            0 -> resources.getString(R.string.link_added)
            1 -> resources.getString(R.string.published)
            2 -> resources.getString(R.string.commented)
            3 -> resources.getString(R.string.related)
            4 -> resources.getString(R.string.digged)
            else -> resources.getString(R.string.buried)
        }
    }
}
