package io.github.feelfreelinux.wykopmobilny.ui.modules.links.hits

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFeedFragment
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkAdapter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.MonthYearPickerDialog
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.YearPickerDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted.PromotedPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted.PromotedView
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.feed_fragment.*
import javax.inject.Inject

data class HitsDataModel(val hits : List<Link>, val currentScreen : String, val yearSelection : Int, val monthSelection : Int)

class HitsFragment : BaseFragment(), HitsView {
    @Inject lateinit var feedAdapter : LinkAdapter
    @Inject lateinit var presenter : HitsPresenter
    lateinit var dataFragment : DataFragment<HitsDataModel>

    companion object {
        val DATA_FRAGMENT_TAG = "HITS_FRAGMENT_TAG"
        val PICKER_REQUEST_CODE = 126

        fun newInstance() : HitsFragment {
            return HitsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.hits_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                swiperefresh.isRefreshing = true
                loadData()
                setTitle()
            }

            R.id.byDay -> {
                swiperefresh.isRefreshing = true
                presenter.currentScreen = HitsPresenter.HITS_DAY
                presenter.loadData()
                setTitle()
            }

            R.id.byWeek -> {
                swiperefresh.isRefreshing = true
                presenter.currentScreen = HitsPresenter.HITS_WEEK
                presenter.loadData()
                setTitle()
            }

            R.id.byMonth -> {
                val pickerFragment = MonthYearPickerDialog.newInstance(presenter.monthSelection, presenter.yearSelection)
                pickerFragment.setTargetFragment(this, PICKER_REQUEST_CODE)
                pickerFragment.show(supportFragmentManager, "pickerDialogFragment")
                setTitle()
            }

            R.id.popular -> {
                swiperefresh.isRefreshing = true
                presenter.currentScreen = HitsPresenter.HITS_POPULAR
                presenter.loadData()
                setTitle()
            }


            R.id.byYear -> {
                val pickerFragment = YearPickerDialog.newInstance(presenter.yearSelection)
                pickerFragment.setTargetFragment(this, PICKER_REQUEST_CODE)
                pickerFragment.show(supportFragmentManager, "pickerDialogFragment")
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        dataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)

        (activity as BaseActivity).supportActionBar?.setTitle(R.string.hits)
        presenter.subscribe(this)
        recyclerView.prepare()
        recyclerView.adapter = feedAdapter
        swiperefresh.setOnRefreshListener { loadData() }
        if (dataFragment.data != null) {
            feedAdapter.addData(dataFragment.data!!.hits, true)
            presenter.apply {
                currentScreen = dataFragment.data!!.currentScreen
                yearSelection = dataFragment.data!!.yearSelection
                monthSelection = dataFragment.data!!.monthSelection
            }
            loadingView.isVisible = false
            feedAdapter.disableLoading()
        } else {
            loadData()
            loadingView.isVisible = true
        }
        setTitle()
    }

    fun loadData() {
        presenter.loadData()
    }

    override fun showHits(links: List<Link>) {
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        feedAdapter.addData(links, true)
        feedAdapter.disableLoading()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dataFragment.data = HitsDataModel(feedAdapter.data, presenter.currentScreen, presenter.yearSelection, presenter.monthSelection)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving) supportFragmentManager.removeDataFragment(dataFragment)
    }

    fun setTitle() {
        (activity as BaseActivity).supportActionBar?.title = when(presenter.currentScreen) {
            HitsPresenter.HITS_POPULAR -> getString(R.string.hits_popular)
            HitsPresenter.HITS_MONTH -> getString(R.string.hits_month_toolbar, presenter.monthSelection, presenter.yearSelection)
            HitsPresenter.HITS_YEAR -> getString(R.string.hits_year_toolbar, presenter.yearSelection)
            HitsPresenter.HITS_WEEK -> getString(R.string.hits_week)
            else -> getString(R.string.hits_day)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICKER_REQUEST_CODE) {
            data?.let {
                if (data.hasExtra(MonthYearPickerDialog.EXTRA_YEAR)) {
                    presenter.yearSelection = data.getIntExtra(MonthYearPickerDialog.EXTRA_YEAR, 2005)
                }

                if (data.hasExtra(MonthYearPickerDialog.EXTRA_MONTH)) {
                    presenter.monthSelection = data.getIntExtra(MonthYearPickerDialog.EXTRA_MONTH, 12)
                    presenter.currentScreen = HitsPresenter.HITS_MONTH
                    swiperefresh.isRefreshing = true
                    presenter.loadData()
                    setTitle()
                } else {
                    presenter.currentScreen = HitsPresenter.HITS_YEAR
                    swiperefresh.isRefreshing = true
                    presenter.loadData()
                    setTitle()
                }

            }
        }
    }
}