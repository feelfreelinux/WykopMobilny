package io.github.feelfreelinux.wykopmobilny.ui.splashscreen
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

class SplashScreenPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val userManager: UserManagerApi, private val userApi: UserApi) : BasePresenter<SplashScreenView>() {
    fun checkIsUserLoggedIn() {
        if (userManager.isUserAuthorized()) {
            getUserToken()
        } else view?.startNavigationActivity()
    }

    private fun getUserToken() {
        subscriptionHelper.subscribe(userApi.getUserSessionToken(),
                {
                    userManager.saveCredentials(it)
                    view?.startNavigationActivity()
                }, { view?.showErrorDialog(it) }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}