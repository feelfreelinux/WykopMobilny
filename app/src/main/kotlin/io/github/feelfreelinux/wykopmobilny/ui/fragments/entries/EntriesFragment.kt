package io.github.feelfreelinux.wykopmobilny.ui.fragments.entries

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import kotlinx.android.synthetic.main.entries_fragment.*

// This fragment shows list of provided entries
class EntriesFragment : BaseFragment(), EntriesFragmentView {
    var refreshDataListener : () -> Unit = {}

    // Inflate view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.entries_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Setup views
        swipeRefresh.setOnRefreshListener(refreshDataListener)


    }
}