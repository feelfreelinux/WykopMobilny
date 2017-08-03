package io.github.feelfreelinux.wykopmobilny.fragments
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.activities.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.adapters.EntryDetailsAdapter
import io.github.feelfreelinux.wykopmobilny.decorators.EntryCommentItemDecoration
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.presenters.EntryDetailsPresenter
import io.github.feelfreelinux.wykopmobilny.callbacks.FeedClickCallbacks
import io.github.feelfreelinux.wykopmobilny.utils.*

class EntryViewFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    lateinit var recyclerView : RecyclerView

    val entryId by lazy {arguments.getInt("ENTRY_ID")}

    lateinit var wam : WykopApiManager
    val navActivity by lazy { activity as NavigationActivity }
    val callbacks by lazy {FeedClickCallbacks(navActivity)}
    val presenter by lazy { EntryDetailsPresenter(wam, callbacks) }
    val adapter by lazy { EntryDetailsAdapter(presenter) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.recycler_view_layout, container, false)
        wam = WykopApiManager(activity)

        // Prepare RecyclerView
        recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)!!
        recyclerView.prepare()

        // Set margin, adapter
        recyclerView.addItemDecoration(EntryCommentItemDecoration(resources.getDimensionPixelOffset(R.dimen.comment_section_left_margin)))
        recyclerView.adapter = adapter

        // Set needed flags
        navActivity.isLoading = true
        navActivity.setSwipeRefreshListener(this)

        // Setup presenter
        presenter.dataLoadedCallback = {
            navActivity.isLoading = false
            navActivity.isRefreshing = false
            adapter.notifyDataSetChanged()
        }

        // Trigger data loading
        presenter.loadData(entryId)
        return view
    }

    override fun onRefresh() =
            presenter.loadData(entryId)


    companion object {
        fun newInstance(id : Int) : Fragment {
            val fragmentData = Bundle()
            val fragment = EntryViewFragment()
            fragmentData.putInt("ENTRY_ID", id)
            fragment.arguments = fragmentData
            return fragment
        }
    }
}