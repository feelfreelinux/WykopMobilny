package io.github.feelfreelinux.wykopmobilny.ui.mainnavigation

import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.utils.api.ApiPreferences
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.api.IApiPreferences
import io.github.feelfreelinux.wykopmobilny.utils.api.getWpisId

class MainNavigationPresenter(val apiManager : WykopApi, val apiPreferences: IApiPreferences) : BasePresenter<MainNavigationView>() {

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
        apiManager.apply {
            getNotificationCount {
                it.fold(
                    { (count) -> view?.notificationCount = count },
                    { view?.showErrorDialog(it) })
            }

            getHashTagsNotificationsCount {
                it.fold(
                        { (count) -> view?.hashTagNotificationCount = count },
                        { view?.showErrorDialog(it) })
            }
        }

    }

}