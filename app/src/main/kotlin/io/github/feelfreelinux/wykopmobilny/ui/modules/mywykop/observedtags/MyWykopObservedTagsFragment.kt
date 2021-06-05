package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.observedtags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObservedTagResponse
import io.github.feelfreelinux.wykopmobilny.ui.adapters.ObservedTagsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.MyWykopNotifier
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.activity_conversations_list.*
import javax.inject.Inject

class MyWykopObservedTagsFragment :
    BaseFragment(),
    MyWykopObservedTagsView,
    androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener,
    MyWykopNotifier {

    companion object {
        const val DATA_FRAGMENT_TAG = "MYWYKOP_OBSERVED_TAGS_FRAGMENT_TAG"

        fun newInstance() = MyWykopObservedTagsFragment()
    }

    @Inject
    lateinit var adapter: ObservedTagsAdapter

    @Inject
    lateinit var presenter: MyWykopObservedTagsPresenter

    lateinit var dataFragment: DataFragment<List<ObservedTagResponse>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_conversations_list, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        dataFragment = childFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        presenter.subscribe(this)
        swiperefresh.setOnRefreshListener(this)
        recyclerView?.prepare()
        recyclerView?.adapter = adapter

        if (dataFragment.data != null) {
            adapter.items.clear()
            adapter.items.addAll(dataFragment.data!!)
            adapter.notifyDataSetChanged()
        } else {
            loadingView.isVisible = true
            presenter.loadTags()
        }
    }

    override fun onRefresh() {
        if (!swiperefresh.isRefreshing) swiperefresh?.isRefreshing = true
        presenter.loadTags()
    }

    override fun showTags(tags: List<ObservedTagResponse>) {
        loadingView.isVisible = false
        swiperefresh?.isRefreshing = false
        adapter.items.clear()
        adapter.items.addAll(tags)
        adapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dataFragment.data = adapter.items
    }

    override fun onDetach() {
        presenter.unsubscribe()
        super.onDetach()
    }

    override fun onPause() {
        if (isRemoving) childFragmentManager.removeDataFragment(dataFragment)
        super.onPause()
    }

    override fun removeDataFragment() = childFragmentManager.removeDataFragment(dataFragment)
}
