package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links

import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.databinding.ProfileSubtabLayoutBinding
import io.github.feelfreelinux.wykopmobilny.utils.viewBinding

class LinksFragment : BaseFragment(R.layout.profile_subtab_layout) {

    companion object {
        fun newInstance() = LinksFragment()
    }

    private val pagerAdapter by lazy { LinksPagerAdapter(resources, childFragmentManager) }

    private val binding by viewBinding(ProfileSubtabLayoutBinding::bind)

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && binding.pager.adapter != pagerAdapter) {
            binding.pager.offscreenPageLimit = 1
            binding.pager.adapter = pagerAdapter
            binding.tabLayout.setupWithViewPager(binding.pager)
        }
    }
}
