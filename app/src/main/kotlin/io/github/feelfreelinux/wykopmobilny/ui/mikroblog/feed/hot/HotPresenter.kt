package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot

import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.api.stream.StreamApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotPresenter(val streamApi: StreamApi) : BasePresenter<HotView>(), BaseFeedPresenter {
    var period = "24"

    override fun loadData(page : Int) {
        val callback = object : Callback<List<Entry>> {
            override fun onResponse(call: Call<List<Entry>>?, response: Response<List<Entry>>?) {
                view?.addDataToAdapter(response!!.body()!!, page == 1)
            }

            override fun onFailure(call: Call<List<Entry>>?, t: Throwable?) {
                view?.showErrorDialog(t!!)
            }
        }

        when (period) {
            "24", "12", "6" -> {
                streamApi.getMirkoblogHot(page, period.toInt()).enqueue(callback)
            }
            "newest" ->
                streamApi.getMikroblogIndex(page).enqueue(callback)
            }
    }
}