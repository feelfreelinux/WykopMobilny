package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.evernote.android.job.util.JobUtil
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseNavigationFragment
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.AppExitConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.USER_LOGGED_IN
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsBroadcastReceiver
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsJob
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.startHashTagsNotificationListActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.startNotificationsListActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.pmlist.openConversationsList
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

fun Context.launchNavigationActivity() {
    startActivity(Intent(this, NavigationActivity::class.java))
}


interface MainNavigationInterface {
    val activityToolbar : Toolbar
    fun openFragment(fragment: Fragment)
    fun showErrorDialog(e: Throwable)
    fun openBrowser(url : String)
}

class NavigationActivity : BaseActivity(), MainNavigationView, NavigationView.OnNavigationItemSelectedListener, MainNavigationInterface {
    override val activityToolbar: Toolbar get() = toolbar
    val LOGIN_REQUEST_CODE = 142

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_mikroblog -> openFragment(HotFragment.newInstance())
            R.id.login -> { openLoginActivity() }
            R.id.messages -> { openConversationsList() }
            else -> presenter.navigationItemClicked(item.itemId)
        }

        item.isChecked = true
        drawer_layout.closeDrawers()
        return true
    }

    @Inject lateinit var presenter : MainNavigationPresenter
    @Inject lateinit var settingsApi : SettingsPreferencesApi

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
        WykopApp.uiInjector.inject(this)
        JobUtil.hasBootPermission(this)

        // Schedules notification service
        WykopNotificationsJob.shedule(settingsApi)

        toolbar.tag = toolbar.overflowIcon // We want to save original overflow icon drawable into memory.
        setupNavigation()
        if (savedInstanceState == null) openMainFragment()
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

    override fun showUsersMenu(value : Boolean) {
        navigationView.menu.apply {
            findItem(R.id.nav_user).isVisible = value
            findItem(R.id.nav_mojwykop).isVisible = value
            findItem(R.id.login).isVisible = !value
            findItem(R.id.logout).isVisible = value
        }
        navHeader.view_container.checkIsUserLoggedIn()
    }

    fun openMainFragment() {
        openFragment(HotFragment())
    }

    override fun openFragment(fragment: Fragment) {
        fab.isVisible = false
        fab.setOnClickListener(null)

        if (fragment is BaseNavigationFragment) fragment.fab = fab

        supportFragmentManager.beginTransaction().replace(R.id.contentView,
                fragment).addToBackStack(fragment.tag).commit()
    }

    override fun closeDrawer() =
            drawer_layout.closeDrawers()

    override fun openBrowser(url: String) {

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            if(drawer_layout.isDrawerOpen(GravityCompat.START)) closeDrawer()
            else AppExitConfirmationDialog(this, { finish() })?.show()
        }
        else supportFragmentManager.popBackStack()
    }

    override fun openLoginActivity() {
        startActivityForResult(Intent(this, LoginScreenActivity::class.java), LOGIN_REQUEST_CODE)
    }

    override fun restartActivity() {
        launchNavigationActivity()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LOGIN_REQUEST_CODE -> {
                if (resultCode == USER_LOGGED_IN) {
                    restartActivity()
                }
            }
        }
    }
}