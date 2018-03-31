package io.github.feelfreelinux.wykopmobilny.ui.modules.blacklist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.BlacklistAdapter
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferences
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.blacklist_fragment.*
import javax.inject.Inject

class BlacklistUsersFragment : BaseFragment() {
    companion object {
        fun createFragment() : Fragment {
            return BlacklistUsersFragment()
        }
    }

    @Inject lateinit var adapter : BlacklistAdapter
    @Inject lateinit var blacklistPreferences : BlacklistPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.blacklist_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.prepare()
        recyclerView.adapter = adapter
        adapter.dataset.clear()
        adapter.dataset.addAll(blacklistPreferences.blockedUsers)
        adapter.notifyDataSetChanged()
    }
}