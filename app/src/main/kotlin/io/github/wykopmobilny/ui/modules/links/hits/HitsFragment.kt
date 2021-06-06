package io.github.wykopmobilny.ui.modules.links.hits

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseActivity
import io.github.wykopmobilny.base.BaseLinksFragment
import io.github.wykopmobilny.ui.dialogs.MonthYearPickerDialog
import io.github.wykopmobilny.ui.dialogs.YearPickerDialog
import javax.inject.Inject

class HitsFragment : BaseLinksFragment(), HitsView {

    companion object {
        const val DATA_FRAGMENT_TAG = "HITS_FRAGMENT_TAG"
        const val PICKER_REQUEST_CODE = 126

        fun newInstance() = HitsFragment()
    }

    @Inject
    lateinit var presenter: HitsPresenter

    override var loadDataListener: (Boolean) -> Unit = {
        presenter.loadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.hits_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                binding.swipeRefresh.isRefreshing = true
                loadDataListener(true)
                setTitle()
            }

            R.id.byDay -> {
                binding.swipeRefresh.isRefreshing = true
                presenter.currentScreen = HitsPresenter.HITS_DAY
                presenter.loadData()
                setTitle()
            }

            R.id.byWeek -> {
                binding.swipeRefresh.isRefreshing = true
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
                binding.swipeRefresh.isRefreshing = true
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.subscribe(this)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.hits)
        linksAdapter.linksActionListener = presenter
        linksAdapter.loadNewDataListener = { loadDataListener(false) }
        setTitle()
        loadDataListener(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    fun setTitle() {
        (activity as BaseActivity).supportActionBar?.title = when (presenter.currentScreen) {
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
                    binding.swipeRefresh.isRefreshing = true
                    presenter.loadData()
                    setTitle()
                } else {
                    presenter.currentScreen = HitsPresenter.HITS_YEAR
                    binding.swipeRefresh.isRefreshing = true
                    presenter.loadData()
                    setTitle()
                }
            }
        }
    }
}
