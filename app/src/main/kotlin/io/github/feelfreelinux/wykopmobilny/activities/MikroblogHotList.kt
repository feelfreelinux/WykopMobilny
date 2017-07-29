package io.github.feelfreelinux.wykopmobilny.activities
import android.os.Bundle
import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager

class MikroblogHotList : MikroblogListActivity() {
    var firstClicked = false
    var period = "24" // Default value


    override fun loadData(page: Int, action: WykopApiManager.WykopApiAction) {
        if (period == "newest") wam.getNewestMikroblog(page, action)
        else wam.getHot( page, period, action)
    }


    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Create hot peroid menu
        menuInflater.inflate(R.menu.hot_period, menu)
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
                    0 -> period = "24"
                    1 -> period = "12"
                    2 -> period = "6"
                    3 -> period = "newest"
                }
                if (firstClicked) { // We don't want to use SwipeRefresh's progressbar on first load
                    swiperefresh.isRefreshing = true
                    onRefresh()
                } else firstClicked = true
            }
        }
        spinner.adapter = adapter
        return super.onCreateOptionsMenu(menu)
    }*/


    companion object {
        fun newInstance(data : WykopApiData) : Fragment {
            val fragmentData = Bundle()
            val fragment = MikroblogHotList()
            fragmentData.putSerializable("wamData", data)
            fragment.arguments = fragmentData
            return fragment
        }
    }
}