package io.github.feelfreelinux.wykopmobilny.activities
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Spinner
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApiManager
import android.widget.ArrayAdapter
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_mikroblog.*


class MikroblogHotList : MikroblogListActivity() {
    var firstClicked = false
    var period = "24" // Default value
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Mikroblog"
    }
    override fun loadData(page: Int, action: WykopApiManager.WykopApiAction) {
        if (period == "newest") wam.getNewestMikroblog(page, action)
        else wam.getHot( page, period, action)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hot_period, menu)
        val item = menu?.findItem(R.id.spinner)
        val spinner = item?.actionView as Spinner
        val adapter = ArrayAdapter<String>(
                supportActionBar?.themedContext,
                R.layout.actionbar_spinner,
                R.id.text1, resources.getStringArray(R.array.hotPeriodSpinner))
        adapter.setDropDownViewResource(R.layout.actionbar_spinner_dropdown)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when(position) {
                    0 -> period = "24"
                    1 -> period = "12"
                    2 -> period = "6"
                    3 -> period = "newest"
                }
                if (firstClicked) {
                    swiperefresh.isRefreshing = true
                    onRefresh()
                } else firstClicked = true
            }
        }
        spinner.adapter = adapter
        return true
    }
}