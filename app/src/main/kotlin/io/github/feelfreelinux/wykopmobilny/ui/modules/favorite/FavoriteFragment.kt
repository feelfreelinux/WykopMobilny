package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragmentNotifier
import kotlinx.android.synthetic.main.activity_mywykop.*

class FavoriteFragment  : BaseFragment() {
    lateinit var pagerAdapter : FavoritePagerAdapter

    companion object {
        fun newInstance(): Fragment {
            return FavoriteFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_mywykop, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagerAdapter = FavoritePagerAdapter(resources, childFragmentManager)
        pager.offscreenPageLimit = 2
        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.favourite)

    }

    override fun onPause() {
        super.onPause()
        if (isRemoving)
        for (i in 0 until pagerAdapter.registeredFragments.size()) {
            (pagerAdapter.registeredFragments.valueAt(i) as FavoriteFragmentNotifier)
                    .removeDataFragment()
        }
    }
}