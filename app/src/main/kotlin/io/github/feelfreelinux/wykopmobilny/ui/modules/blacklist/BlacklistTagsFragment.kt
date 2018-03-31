package io.github.feelfreelinux.wykopmobilny.ui.modules.blacklist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.scraper.Blacklist
import io.github.feelfreelinux.wykopmobilny.ui.adapters.BlacklistAdapter
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferences
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.blacklist_fragment.*
import javax.inject.Inject

class BlacklistTagsFragment : BaseFragment() {
    @Inject lateinit var adapter : BlacklistAdapter
    @Inject lateinit var blacklistPreferences : BlacklistPreferences
    val disposable = CompositeDisposable()
    companion object {
        fun createFragment() : Fragment {
            return BlacklistTagsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.blacklist_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.isBlockUser = false
        adapter.unblockListener = {
            (activity as BlacklistActivity).unblockTag(it)
        }
        adapter.blockListener = {
            (activity as BlacklistActivity).blockTag(it)
        }
        recyclerView.prepare()
        recyclerView.adapter = adapter
        updateData()
    }

    fun updateData() {
        adapter.dataset.clear()
        adapter.dataset.addAll(blacklistPreferences.blockedTags)
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposable.add((activity as BlacklistActivity).updateDataSubject.subscribe {
            updateData()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}