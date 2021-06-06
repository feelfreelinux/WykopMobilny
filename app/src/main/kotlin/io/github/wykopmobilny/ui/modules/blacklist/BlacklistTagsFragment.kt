package io.github.wykopmobilny.ui.modules.blacklist

import android.os.Bundle
import android.view.View
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseFragment
import io.github.wykopmobilny.databinding.BlacklistFragmentBinding
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import io.github.wykopmobilny.ui.adapters.BlacklistAdapter
import io.github.wykopmobilny.utils.prepare
import io.github.wykopmobilny.utils.viewBinding
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BlacklistTagsFragment : BaseFragment(R.layout.blacklist_fragment) {

    companion object {
        fun createFragment() = BlacklistTagsFragment()
    }

    @Inject
    lateinit var adapter: BlacklistAdapter

    @Inject
    lateinit var blacklistPreferences: BlacklistPreferencesApi

    private val binding by viewBinding(BlacklistFragmentBinding::bind)

    private val disposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.isBlockUser = false
        adapter.unblockListener = { (activity as BlacklistActivity).unblockTag(it) }
        adapter.blockListener = { (activity as BlacklistActivity).blockTag(it) }
        binding.recyclerView.prepare()
        binding.recyclerView.adapter = adapter
        updateData()
    }

    private fun updateData() {
        adapter.items.clear()
        adapter.items.addAll(blacklistPreferences.blockedTags)
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposable.add((activity as BlacklistActivity).updateDataSubject.subscribe { updateData() })
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
