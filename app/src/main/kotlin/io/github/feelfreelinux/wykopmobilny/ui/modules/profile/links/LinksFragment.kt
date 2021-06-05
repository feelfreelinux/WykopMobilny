package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import kotlinx.android.synthetic.main.profile_subtab_layout.*

class LinksFragment : BaseFragment() {

    companion object {
        fun newInstance() = LinksFragment()
    }

    private val pagerAdapter by lazy { LinksPagerAdapter(resources, childFragmentManager) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_subtab_layout, container, false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && pager.adapter != pagerAdapter) {
            pager.offscreenPageLimit = 1
            pager.adapter = pagerAdapter
            tabLayout.setupWithViewPager(pager)
        }
    }
}
