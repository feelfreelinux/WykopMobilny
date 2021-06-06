package io.github.wykopmobilny.ui.modules.profile.microblog

import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseFragment
import io.github.wykopmobilny.databinding.ProfileSubtabLayoutBinding
import io.github.wykopmobilny.utils.viewBinding

class MicroblogFragment : BaseFragment(R.layout.profile_subtab_layout) {

    companion object {
        const val DATA_FRAGMENT_TAG = "USER_MICROBLOG"
        fun newInstance() = MicroblogFragment()
    }

    private val binding by viewBinding(ProfileSubtabLayoutBinding::bind)

    private val pagerAdapter by lazy { MicroblogPagerAdapter(resources, childFragmentManager) }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && binding.pager.adapter != pagerAdapter) {
            binding.pager.offscreenPageLimit = 1
            binding.pager.adapter = pagerAdapter
            binding.tabLayout.setupWithViewPager(binding.pager)
        }
    }
}
