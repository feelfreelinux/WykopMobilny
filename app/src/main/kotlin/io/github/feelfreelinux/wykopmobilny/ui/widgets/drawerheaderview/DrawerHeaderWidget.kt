package io.github.feelfreelinux.wykopmobilny.ui.widgets.drawerheaderview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserCredentials
import kotlinx.android.synthetic.main.drawer_header_view_layout.view.*

class DrawerHeaderWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.constraintlayout.widget.ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        // @TODO Put this in memory
        private const val PLACEHOLDER_IMAGE_URL = "https://i.imgur.com/aSm6pSJ.jpg"
    }

    init {
        View.inflate(context, R.layout.drawer_header_view_layout, this)
    }

    var hashTagsNotificationsCount: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {
            nav_notifications_tag.text = value.toString()
        }

    var notificationCount: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {
            nav_notifications.text = value.toString()
        }

    fun showDrawerHeader(isUserAuthorized: Boolean, credentials: UserCredentials?) {
        if (isUserAuthorized) {
            isVisible = true
            nav_profile_image.loadImage(credentials!!.avatarUrl)
            nav_profile_image.setOnClickListener {
                val context = getActivityContext()!!
                context.startActivity(ProfileActivity.createIntent(context, credentials.login))
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