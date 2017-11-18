package io.github.feelfreelinux.wykopmobilny.ui.widgets.drawerheaderview

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.drawer_header_view_layout.view.*
import javax.inject.Inject

class DrawerHeaderWidget : ConstraintLayout, DrawerHeaderView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    // @TODO Put this in memory
    val PLACEHOLDER_IMAGE_URL = "https://i.imgur.com/aSm6pSJ.jpg"

    @Inject lateinit var userManager : UserManagerApi
    @Inject lateinit var presenter : DrawerHeaderPresenter

    init {
        View.inflate(context, R.layout.drawer_header_view_layout, this)
        WykopApp.uiInjector.inject(this)
    }

    override var hashTagsNotificationsCount: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) { nav_notifications_tag.text = value.toString() }

    override var notificationCount: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) { nav_notifications.text = value.toString() }

    private fun checkIsUserLoggedIn() {
        if (userManager.isUserAuthorized()) {
            isVisible = true
            val credentials = userManager.getUserCredentials()!!
            nav_profile_image.loadImage(credentials.avatarUrl)
            if (credentials.backgroundUrl != null) {
                backgroundImage.loadImage(credentials.backgroundUrl)
            } else {
                backgroundImage.loadImage(PLACEHOLDER_IMAGE_URL)
            }
            presenter.fetchNotifications()
        } else {
            isVisible = false
        }
    }

    fun startListeningForUpdates() {
        if (userManager.isUserAuthorized()) {
            presenter.subscribe(this)
            checkIsUserLoggedIn()
        }
    }

    fun stopListeningForUpdates() {
        if (userManager.isUserAuthorized()) {
            presenter.unsubscribe()
        }
    }

    override fun onDetachedFromWindow() {
        presenter.unsubscribe()
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.subscribe(this)
        checkIsUserLoggedIn()
    }

    override val isConnectedToInternet : Boolean
        get() = context.isConnectedToInternet()

    override fun showErrorDialog(e: Throwable) = context.showExceptionDialog(e)
}