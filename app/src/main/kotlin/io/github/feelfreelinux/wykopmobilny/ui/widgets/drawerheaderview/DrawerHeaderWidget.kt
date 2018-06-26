package io.github.feelfreelinux.wykopmobilny.ui.widgets.drawerheaderview

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserCredentials
import kotlinx.android.synthetic.main.drawer_header_view_layout.view.*

class DrawerHeaderWidget : androidx.constraintlayout.widget.ConstraintLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    // @TODO Put this in memory
    val PLACEHOLDER_IMAGE_URL = "https://i.imgur.com/aSm6pSJ.jpg"

    init {
        View.inflate(context, R.layout.drawer_header_view_layout, this)
    }

    var hashTagsNotificationsCount: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) { nav_notifications_tag.text = value.toString() }

    var notificationCount: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) { nav_notifications.text = value.toString() }

    fun showDrawerHeader(isUserAuthorized : Boolean, credentials: UserCredentials?) {
        if (isUserAuthorized) {
            isVisible = true
            nav_profile_image.loadImage(credentials!!.avatarUrl)
            nav_profile_image.setOnClickListener {
                getActivityContext()!!.startActivity(ProfileActivity.createIntent(getActivityContext()!!, credentials.login))
            }
            if (credentials.backgroundUrl != null) {
                backgroundImage.loadImage(credentials.backgroundUrl)
            } else {
                backgroundImage.loadImage(PLACEHOLDER_IMAGE_URL)
            }
        } else {
            isVisible = false
        }
    }
}