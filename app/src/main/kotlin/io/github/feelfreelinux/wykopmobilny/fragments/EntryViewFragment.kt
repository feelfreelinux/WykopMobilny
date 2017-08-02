package io.github.feelfreelinux.wykopmobilny.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.activities.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.adapters.EntryDetailsAdapter
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.objects.EntryDetails
import io.github.feelfreelinux.wykopmobilny.projectors.FeedClickActions
import io.github.feelfreelinux.wykopmobilny.utils.*

class EntryViewFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private val kodein = LazyKodein(appKodein)
    lateinit var recyclerView: RecyclerView

    private val entryId by lazy { arguments.getInt("ENTRY_ID") }

    private val apiManager: WykopApiManager by kodein.instance()
    private val navActivity by lazy { activity as NavigationActivity }
    private val entryAdapter by lazy { EntryDetailsAdapter(FeedClickActions(navActivity)) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.recycler_view_layout, container, false)

        // Prepare RecyclerView, and EndlessScrollListener
        recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)!!
        recyclerView.prepare()

        // Create adapter

        recyclerView.adapter = entryAdapter

        // Set needed flags
        navActivity.isLoading = true
        navActivity.setSwipeRefreshListener(this)

        // Trigger data loading
        onRefresh()
        return view
    }

    override fun onRefresh() {
        loadData({
            result ->
            run {
                entryAdapter.entryData = emptyList()
                addDataToAdapter(result)
            }
        })
    }

    fun loadData(responseCallback: (List<Entry>) -> Unit) {
        apiManager.getEntryIndex(entryId, {
            result ->
            run {
                val list = ArrayList<Entry>()
                val details = result as EntryDetails
                list.add(parseEntry(details))
                details.comments.mapTo(list) { parseEntry(it) }
                responseCallback.invoke(list)
            }
        })
    }

    fun addDataToAdapter(list: List<Entry>) {
        val fullList = ArrayList<Entry>()
        fullList.addAll(entryAdapter.entryData)
        fullList.addAll(list)
        entryAdapter.entryData = fullList
        (activity as NavigationActivity).run {
            isLoading = false
            isRefreshing = false
        }

    }

    companion object {
        fun newInstance(id: Int): Fragment {
            val fragmentData = Bundle()
            val fragment = EntryViewFragment()
            fragmentData.putInt("ENTRY_ID", id)
            fragment.arguments = fragmentData
            return fragment
        }
    }
}