package io.github.feelfreelinux.wykopmobilny.ui.widgets.drawerheaderview

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.startHashTagsNotificationListActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.startNotificationsListActivity
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.drawer_header_view_layout.view.*
import javax.inject.Inject

class DrawerHeaderWidget : ConstraintLayout, DrawerHeaderView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject lateinit var userManager : UserManagerApi
    @Inject lateinit var presenter : DrawerHeaderPresenter

    init {
        View.inflate(context, R.layout.drawer_header_view_layout, this)
        WykopApp.uiInjector.inject(this)
        nav_notifications.setOnClickListener {
            context.startNotificationsListActivity()
        }

        nav_notifications_tag.setOnClickListener {
            context.startHashTagsNotificationListActivity()
        }
    }

    override var hashTagsNotificationsCount: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) { nav_notifications_tag.text = value.toString() }

    override var notificationCount: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) { nav_notifications.text = value.toString() }

    fun checkIsUserLoggedIn() {
        if (userManager.isUserAuthorized()) {
            isVisible = true
            img_profile.loadImage(userManager.getUserCredentials()!!.avatarUrl)
            presenter.fetchNotifications()
        } else isVisible = false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unsubscribe()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.subscribe(this)
    }

    override fun showErrorDialog(e: Throwable) = context.showExceptionDialog(e)
}