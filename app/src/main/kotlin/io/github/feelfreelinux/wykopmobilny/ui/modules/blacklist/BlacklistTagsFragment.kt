package io.github.feelfreelinux.wykopmobilny.ui.modules.blacklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.BlacklistAdapter
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferences
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.blacklist_fragment.*
import javax.inject.Inject

class BlacklistTagsFragment : BaseFragment() {

    companion object {
        fun createFragment() = BlacklistTagsFragment()
    }

    @Inject lateinit var adapter: BlacklistAdapter

    @Inject lateinit var blacklistPreferences: BlacklistPreferences

    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.blacklist_fragment, container, false)

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

    private fun updateData() {
        adapter.items.clear()
        adapter.items.addAll(blacklistPreferences.blockedTags)
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposable.add(
            (activity as BlacklistActivity).updateDataSubject.subscribe {
                updateData()
            }
        )
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
