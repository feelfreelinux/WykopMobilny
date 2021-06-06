package io.github.wykopmobilny.ui.modules.profile.links.related

import android.os.Bundle
import android.view.View
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseFragment
import io.github.wykopmobilny.databinding.FeedFragmentBinding
import io.github.wykopmobilny.models.dataclass.Related
import io.github.wykopmobilny.ui.adapters.ProfileRelatedAdapter
import io.github.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.wykopmobilny.utils.prepare
import io.github.wykopmobilny.utils.viewBinding
import javax.inject.Inject

class ProfileRelatedFragment : BaseFragment(R.layout.feed_fragment), ProfileRelatedView {

    companion object {
        fun newInstance() = ProfileRelatedFragment()
    }

    private val username by lazy { (activity as ProfileActivity).username }

    @Inject
    lateinit var feedAdapter: ProfileRelatedAdapter

    @Inject
    lateinit var presenter: ProfileRelatedPresenter

    private val binding by viewBinding(FeedFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.subscribe(this)
        presenter.username = username
        binding.recyclerView.prepare()
        feedAdapter.loadNewDataListener = { presenter.loadData(false) }
        binding.recyclerView.adapter = feedAdapter
        presenter.loadData(true)
    }

    override fun addDataToAdapter(entryList: List<Related>, shouldClearAdapter: Boolean) =
        feedAdapter.addData(entryList, shouldClearAdapter)

    override fun disableLoading() = feedAdapter.disableLoading()
}
