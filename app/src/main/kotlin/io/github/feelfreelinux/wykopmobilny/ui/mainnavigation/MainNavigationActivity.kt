package io.github.feelfreelinux.wykopmobilny.ui.mainnavigation

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.add_user_input.*
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.AppExitConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.android.synthetic.main.toolbar.*

fun Context.launchNavigationActivity() {
    startActivity(Intent(this, NavigationActivity::class.java))
}


interface MainNavigationInterface {
    val activityToolbar : Toolbar
    fun openFragment(fragment: Fragment)
    fun showErrorDialog(e: Exception)
    fun openBrowser(url : String)
    fun openNewEntryUserInput(receiver : String?)
    fun openNewEntryCommentUserInput(entryId : Int, receiver: String?)
}

class NavigationActivity : BaseActivity(), MainNavigationContract.View, NavigationView.OnNavigationItemSelectedListener, MainNavigationInterface {
    override var actionUrl: Uri? = null
    override val activityToolbar: Toolbar get() = toolbar

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        presenter.navigationItemClicked(item.itemId)
        item.isChecked = true
        drawer_layout.closeDrawers()
        return true
    }


    private val presenter by lazy {
        MainNavigationPresenter(kodein.instanceValue(), kodein.instanceValue())
    }

    private val navHeader by lazy { navigationView.getHeaderView(0) }
    private val actionBarToggle by lazy {
        ActionBarDrawerToggle(this,
                drawer_layout,
                toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_closed)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)
        toolbar.tag = toolbar.overflowIcon // We want to save original overflow icon drawable into memory.

        actionUrl = intent.data
        setupNavigation()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        actionBarToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (actionBarToggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    private fun setupNavigation() {
        drawer_layout.addDrawerListener(actionBarToggle)
        navigationView.setNavigationItemSelectedListener(this)
        presenter.subscribe(this)
    }


    override var notificationCount: Int
        get() = navHeader.nav_notifications.text.toString().toInt()
        set(value) { navHeader.nav_notifications.text = value.toString() }

    override var hashTagNotificationCount: Int
        get() = navHeader.nav_notifications_tag.text.toString().toInt()
        set(value) { navHeader.nav_notifications_tag.text = value.toString() }

    override var avatarUrl: String
        get() = TODO("not implemented")
        set(value) {
            navHeader.img_profile.loadImage(value)
        }


    override fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.contentView,
                fragment).addToBackStack(fragment.tag).commit()
    }

    override fun closeDrawer() =
            drawer_layout.closeDrawers()

    override fun openBrowser(url: String) {

    }

    override fun openNewEntryUserInput(receiver: String?) {
        launchNewEntryUserInput(receiver)
    }

    override fun openNewEntryCommentUserInput(entryId : Int, receiver: String?) {
        launchEntryCommentUserInput(entryId, receiver)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            if(drawer_layout.isDrawerOpen(GravityCompat.START)) closeDrawer()
            else AppExitConfirmationDialog(this, { finish() })?.show()
        }
        else supportFragmentManager.popBackStack()
    }
}