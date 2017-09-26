package io.github.feelfreelinux.wykopmobilny.ui.splashscreen
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class SplashScreenPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val apiPreferences: CredentialsPreferencesApi, private val userApi: UserApi) : BasePresenter<SplashScreenView>() {
    fun checkIsUserLoggedIn() {
        if (apiPreferences.isUserAuthorized()) {
            getUserToken()
        } else view?.startNavigationActivity()
    }

    fun getUserToken() {
        subscriptions.add(
                subscriptionHelper.subscribeOnSchedulers(userApi.getUserSessionToken())
                        .subscribe({
                            apiPreferences.userToken = it.userKey
                            apiPreferences.avatarUrl = it.avatarBig
                            view?.startNavigationActivity()
                        }, { view?.showErrorDialog(it) })
        )
    }
}