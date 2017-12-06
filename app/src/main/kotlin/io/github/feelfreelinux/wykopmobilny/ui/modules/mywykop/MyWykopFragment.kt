package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseNavigationFragment
import kotlinx.android.synthetic.main.activity_mywykop.*

class MyWykopFragment : BaseNavigationFragment() {
    lateinit var pagerAdapter : MyWykopPagerAdapter

    companion object {
        fun newInstance(): Fragment {
            return MyWykopFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.activity_mywykop, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        pagerAdapter = MyWykopPagerAdapter(resources, childFragmentManager)
        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.mywykop)

    }
}