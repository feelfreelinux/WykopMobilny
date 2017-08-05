package io.github.feelfreelinux.wykopmobilny.fragments
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.presenters.HotFeedPresenter

class HotFeedFragment : FeedFragment() {
    override val feedPresenter by lazy { HotFeedPresenter(apiManager, callbacks) }
    val supportActionBar by lazy{ (activity as AppCompatActivity).supportActionBar }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        fun newInstance() : Fragment {
            return HotFeedFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Create hot peroid menu
        inflater?.inflate(R.menu.hot_period, menu)
        val item = menu?.findItem(R.id.spinner)
        val spinner = item?.actionView as Spinner
        val adapter = ArrayAdapter<String>(
                supportActionBar?.themedContext,
                R.layout.actionbar_spinner,
                R.id.text1, resources.getStringArray(R.array.hotPeriodSpinner))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when(position) {
                    0 -> feedPresenter.period = "24"
                    1 -> feedPresenter.period = "12"
                    2 -> feedPresenter.period = "6"
                    3 -> feedPresenter.period = "newest"
                }
            }
        }
        spinner.adapter = adapter
        super.onCreateOptionsMenu(menu, inflater)
    }
}