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
import android.view.View
import android.widget.Toast
import com.evernote.android.job.util.JobUtil
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.UpdateFrom
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseNavigationView
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.AppExitConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted.PromotedFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.MyWykopFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsJob
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.HashTagsNotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.settings.SettingsActivity
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.drawer_header_view_layout.view.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

interface MainNavigationInterface {
    val activityToolbar : Toolbar
    fun openFragment(fragment: Fragment)
    fun showErrorDialog(e: Throwable)
    val floatingButton : View
}

class MainNavigationActivity : BaseActivity(), MainNavigationView, NavigationView.OnNavigationItemSelectedListener, MainNavigationInterface {
    override val activityToolbar: Toolbar get() = toolbar

    override val floatingButton: View
        get() = fab

    companion object {
        val LOGIN_REQUEST_CODE = 142
        val TARGET_FRAGMENT_KEY = "TARGET_FRAGMENT"
        val TARGET_NOTIFICATIONS = "TARGET_NOTIFICATIONS"

        fun getIntent(context: Context, targetFragment: String? = null): Intent {
            val intent = Intent(context, MainNavigationActivity::class.java)
            targetFragment?.let {
                intent.putExtra(MainNavigationActivity.TARGET_FRAGMENT_KEY, targetFragment)
            }
            return intent
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_mikroblog -> openFragment(HotFragment.newInstance())
            R.id.login -> { navigator.openLoginScreen(this, LOGIN_REQUEST_CODE) }
            R.id.messages -> { openFragment(ConversationsListFragment.newInstance()) }
            R.id.nav_settings -> { navigator.openSettingsActivity(this) }
            R.id.nav_mojwykop -> { openFragment(MyWykopFragment.newInstance()) }
            R.id.nav_home -> { openFragment(PromotedFragment.newInstance()) }
            R.id.search -> { openFragment(SearchFragment.newInstance()) }
            R.id.logout -> {
                userManagerApi.logoutUser()
                restartActivity()
            }
            else -> showNotImplementedToast()
        }

        item.isChecked = true
        drawer_layout.closeDrawers()
        return true
    }

    @Inject lateinit var presenter : MainNavigationPresenter
    @Inject lateinit var settingsApi : SettingsPreferencesApi
    @Inject lateinit var navigator : NavigatorApi
    @Inject lateinit var newNavigator : NewNavigatorApi
    @Inject lateinit var userManagerApi : UserManagerApi


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
        JobUtil.hasBootPermission(this)

        // Setup AppUpdater
        AppUpdater(this)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("feelfreelinux", "WykopMobilny")
                .setTitleOnUpdateAvailable(R.string.update_available)
                .setContentOnUpdateAvailable(R.string.update_app)
                .setButtonDismiss(R.string.cancel)
                .setButtonDoNotShowAgain(R.string.do_not_show_again)
                .setButtonUpdate(R.string.update)
                .start()

        if (settingsApi.showNotifications) {
            // Schedules notification service
            WykopNotificationsJob.schedule(settingsApi)
        }
        presenter.subscribe(this)

        toolbar.tag = toolbar.overflowIcon // We want to save original overflow icon drawable into memory.
        navHeader.view_container?.showDrawerHeader(userManagerApi.isUserAuthorized(), userManagerApi.getUserCredentials())

        showUsersMenu(userManagerApi.isUserAuthorized())
        if (savedInstanceState == null) {
            if (intent.hasExtra(TARGET_FRAGMENT_KEY)) {
                when (intent.getStringExtra(TARGET_FRAGMENT_KEY)) {
                    TARGET_NOTIFICATIONS -> openFragment(NotificationsListFragment.newInstance())
                }

            } else openMainFragment()
        }
        presenter.startListeningForNotifications()
        setFABVisibility()
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
        printout("UNSUBSCRIBE")
    }

    private fun setupNavigation() {
        drawer_layout.addDrawerListener(actionBarToggle)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun showUsersMenu(value : Boolean) {
        navigationView.menu.apply {
            findItem(R.id.nav_user).isVisible = value
            findItem(R.id.nav_mojwykop).isVisible = value
            findItem(R.id.login).isVisible = !value
            findItem(R.id.logout).isVisible = value
        }
        setFABVisibility()
        navHeader.view_container.isVisible = value
        navHeader.view_container.apply {
            nav_notifications_tag.setOnClickListener {
                openFragment(HashTagsNotificationsListFragment.newInstance())
                deselectItems()
            }

            nav_notifications.setOnClickListener {
                openFragment(NotificationsListFragment.newInstance())
                deselectItems()
            }
        }
    }

    fun openMainFragment() {
        when (settingsApi.defaultScreen!!) {
            "mainpage" -> openFragment(PromotedFragment.newInstance())
            "mikroblog" -> openFragment(HotFragment.newInstance())
        }
        openFragment(HotFragment.newInstance())
    }

    override fun openFragment(fragment: Fragment) {
        fab.isVisible = false
        fab.setOnClickListener(null)
        if (fragment is BaseNavigationView) fab.isVisible = true
        supportFragmentManager.beginTransaction().replace(R.id.contentView,
                fragment, "MAIN_FRAGMENT").commit()
        closeDrawer()
    }

    fun setFABVisibility() {
        val fragment = supportFragmentManager.findFragmentById(R.id.contentView)
        fab.isVisible = fragment != null && fragment is BaseNavigationView && userManagerApi.isUserAuthorized()
    }

    fun closeDrawer() =
            drawer_layout.closeDrawers()

    fun deselectItems() {
        val menu = navigationView.menu
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
    }

    override fun showNotificationsCount(notifications: Int) {
        drawer_layout.view_container.apply {
            notificationCount = notifications
        }

    }

    override fun showHashNotificationsCount(hashNotifications: Int) {
        drawer_layout.view_container.apply {
            hashTagsNotificationsCount = hashNotifications
        }

    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)) closeDrawer()
        else AppExitConfirmationDialog(this, { finish() }).show()
    }

    override fun restartActivity() {
        navigator.openMainActivity(this)
        finish()
    }

    override fun showNotImplementedToast() {
        Toast.makeText(this, "Nie zaimplementowano", Toast.LENGTH_SHORT).show()
        deselectItems()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LOGIN_REQUEST_CODE -> {
                if (resultCode == LoginScreenActivity.USER_LOGGED_IN) {
                    restartActivity()
                }
            }

            NewNavigator.STARTED_FROM_NOTIFICATIONS_CODE -> {
                printout("dddd")
                presenter.startListeningForNotifications()
            }

            else -> {
                if (resultCode == SettingsActivity.THEME_CHANGED_RESULT) {
                    restartActivity()
                }
            }
        }
    }
}