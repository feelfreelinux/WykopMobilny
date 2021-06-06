package io.github.wykopmobilny.ui.modules.mywykop

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseActivity
import io.github.wykopmobilny.base.BaseFragment
import io.github.wykopmobilny.databinding.ActivityMywykopBinding
import io.github.wykopmobilny.utils.viewBinding

class MyWykopFragment : BaseFragment(R.layout.activity_mywykop) {

    companion object {
        fun newInstance() = MyWykopFragment()
    }

    lateinit var pagerAdapter: MyWykopPagerAdapter

    private val binding by viewBinding(ActivityMywykopBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = MyWykopPagerAdapter(resources, childFragmentManager)
        binding.pager.offscreenPageLimit = 4
        binding.pager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.pager)

        (activity as BaseActivity).supportActionBar?.setTitle(R.string.mywykop)
    }

    fun onRefresh() {
        for (i in 0 until pagerAdapter.registeredFragments.size()) {
            (pagerAdapter.registeredFragments.valueAt(i) as? SwipeRefreshLayout.OnRefreshListener)?.onRefresh()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> onRefresh()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.refresh_layout, menu)
    }
}
