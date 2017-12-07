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
import android.widget.Toast
import com.bugsnag.android.Bugsnag
import com.evernote.android.job.util.JobUtil
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.UpdateFrom
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseNavigationFragment
import io.github.feelfreelinux.wykopmobilny.base.BaseNavigationView
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.AppExitConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.Navigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsJob
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.HashTagsNotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.settings.SettingsActivity
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.drawer_header_view_layout.view.*
import kotlinx.android.synthetic.main.navigation_header.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

interface MainNavigationInterface {
    val activityToolbar : Toolbar
    fun openFragment(fragment: Fragment)
    fun showErrorDialog(e: Throwable)
}

class NavigationActivity : BaseActivity(), MainNavigationView, NavigationView.OnNavigationItemSelectedListener, MainNavigationInterface {
    override val activityToolbar: Toolbar get() = toolbar

    companion object {
        val LOGIN_REQUEST_CODE = 142
        val TARGET_FRAGMENT_KEY = "TARGET_FRAGMENT"
        val TARGET_NOTIFICATIONS = "TARGET_NOTIFICATIONS"

        fun getIntent(context: Context, targetFragment: String? = null): Intent {
            val intent = Intent(context, NavigationActivity::class.java)
            targetFragment?.let {
                intent.putExtra(NavigationActivity.TARGET_FRAGMENT_KEY, targetFragment)
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
            else -> presenter.navigationItemClicked(item.itemId)
        }

        item.isChecked = true
        drawer_layout.closeDrawers()
        return true
    }

    @Inject lateinit var presenter : MainNavigationPresenter
    @Inject lateinit var settingsApi : SettingsPreferencesApi
    @Inject lateinit var navigator : NavigatorApi


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

        // Setup AppUpdater
        AppUpdater(this)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
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
        navHeader.view_container?.startListeningForUpdates()

        toolbar.tag = toolbar.overflowIcon // We want to save original overflow icon drawable into memory.
        setupNavigation()
        if (savedInstanceState == null) {
            if (intent.hasExtra(TARGET_FRAGMENT_KEY)) {
                when (intent.getStringExtra(TARGET_FRAGMENT_KEY)) {
                    TARGET_NOTIFICATIONS -> openFragment(NotificationsListFragment.newInstance())
                }

            } else openMainFragment()
        }
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

    override fun onResume() {
        super.onResume()
        presenter.subscribe(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        navHeader.view_container?.stopListeningForUpdates()
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
        fab.isVisible = value
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
        // @TODO Handle settings here
        openFragment(HotFragment())
    }

    override fun openFragment(fragment: Fragment) {
        fab.isVisible = false
        fab.setOnClickListener(null)

        if (fragment is BaseNavigationView) fragment.fab = fab

        supportFragmentManager.beginTransaction().replace(R.id.contentView,
                fragment).commit()
        closeDrawer()
    }

    fun closeDrawer() =
            drawer_layout.closeDrawers()

    fun deselectItems() {
        val menu = navigationView.menu
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
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

            Navigator.STARTED_FROM_NOTIFIATIONS_CODE -> {
                view_container?.startListeningForUpdates()
            }

            else -> {
                if (resultCode == SettingsActivity.THEME_CHANGED_RESULT) {
                    restartActivity()
                }
            }
        }
    }
}