package io.github.feelfreelinux.wykopmobilny.fragments
import android.os.Bundle
import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData

class HotFeedFragment : FeedFragment() {
    var period = "24"

    override fun loadData(page : Int, resultCallback : (Any) -> Unit) {
        wam.getMikroblogHot(page, period, resultCallback)
    }

    companion object {
        fun newInstance(data : WykopApiData) : Fragment {
            val fragmentData = Bundle()
            val fragment = HotFeedFragment()
            fragmentData.putSerializable("wamData", data)
            fragment.arguments = fragmentData
            return fragment
        }
    }
}