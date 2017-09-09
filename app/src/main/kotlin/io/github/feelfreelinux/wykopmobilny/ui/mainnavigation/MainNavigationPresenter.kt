package io.github.feelfreelinux.wykopmobilny.ui.mainnavigation

import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.Presenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry.EntryFragment
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.utils.api.ApiPreferences
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.utils.api.IApiPreferences
import io.github.feelfreelinux.wykopmobilny.utils.api.getWpisId

class MainNavigationPresenter(val apiManager : WykopApi, val apiPreferences: IApiPreferences) : Presenter<MainNavigationContract.View>(), MainNavigationContract.Presenter {
    override fun subscribe(view: MainNavigationContract.View) {
        super.subscribe(view)
        setupNavigation()
    }

    override fun navigationItemClicked(itemId: Int) {
        when (itemId) {
            R.id.nav_mikroblog -> view?.openFragment(HotFragment.newInstance())
        }
    }

    private fun setupNavigation() {
        apiPreferences.avatarUrl?.let { view?.avatarUrl = it }
        getNotificationsCount()
        if (view?.actionUrl == null) view?.openFragment(HotFragment.newInstance())
        else view?.openFragment(EntryFragment.newInstance(view?.actionUrl!!.getWpisId()))
    }

    override fun getNotificationsCount() {
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