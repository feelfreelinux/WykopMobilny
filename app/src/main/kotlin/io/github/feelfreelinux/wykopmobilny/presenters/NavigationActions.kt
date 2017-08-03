package io.github.feelfreelinux.wykopmobilny.presenters

import android.support.v4.app.Fragment
import android.support.design.widget.NavigationView
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.fragments.HotFeedFragment
import io.github.feelfreelinux.wykopmobilny.activities.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.objects.NotificationCountResponse
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.navigation_header.view.*

class NavigationActions(val context : NavigationActivity) : NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_mikroblog -> 
                openFragment(HotFeedFragment.newInstance())
        }
        item.isChecked = true
        context.drawer_layout.closeDrawers()
        return true
    }

    fun setupNavigation() {
        context.drawer_layout.addDrawerListener(context.actionBarToggle)
        context.navigationView.setNavigationItemSelectedListener(this)
        setupHeader()
        openInitialFragment()
    }

    fun setupHeader() {
        context.navHeader.img_profile.loadImage(context.wam.apiPrefs.avatarUrl!!)
        getNotificationsCount()
    }

    fun openFragment(fragment : Fragment) {
        context.supportFragmentManager.beginTransaction().replace(R.id.contentView,
                fragment).addToBackStack(fragment.tag).commit()
    }

    fun openInitialFragment() {
        context.supportFragmentManager.beginTransaction().replace(R.id.contentView,
                HotFeedFragment.newInstance()).commit()
    }

    fun getNotificationsCount() {
        context.wam.getNotificationCount({
            result -> context.navHeader.nav_notifications.text =
                (result as NotificationCountResponse).count.toString()})
        context.wam.getHashTagsNotificationsCount({
            result -> context.navHeader.nav_notifications_tag.text =
                (result as NotificationCountResponse).count.toString()})    }
}