package io.github.feelfreelinux.wykopmobilny.ui.mainnavigation

import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.api.enqueue

class MainNavigationPresenter(private val apiPreferences: CredentialsPreferencesApi, private val myWykopApi: MyWykopApi) : BasePresenter<MainNavigationView>() {

    fun navigationItemClicked(itemId: Int) {
        when (itemId) {
            R.id.nav_mikroblog -> view?.openFragment(HotFragment.newInstance())
        }
    }

    override fun subscribe(view: MainNavigationView) {
        super.subscribe(view)
        setupNavigation()
    }

    private fun setupNavigation() {
        apiPreferences.avatarUrl?.let { view?.avatarUrl = it }
        getNotificationsCount()
        view?.openFragment(HotFragment.newInstance())
    }

    fun getNotificationsCount() {
        myWykopApi.apply {
            getNotificationCount().enqueue (
                { view?.notificationCount = it.body()!!.count },
                { view?.showErrorDialog(it) }
            )

            getHashTagNotificationCount().enqueue(
                    { view?.hashTagNotificationCount = it.body()!!.count },
                    { view?.showErrorDialog(it) }
            )
        }
    }

}