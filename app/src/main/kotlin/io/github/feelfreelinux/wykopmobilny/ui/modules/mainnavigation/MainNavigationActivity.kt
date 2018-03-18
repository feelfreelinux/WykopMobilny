package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.internal.NavigationMenuView
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
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
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.WykopMobilnyUpdate
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.AppExitConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.FavoriteFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.hits.HitsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted.PromotedFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.upcoming.UpcomingFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.MyWykopFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsJob
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.NotificationsListActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.HashTagsNotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.settings.SettingsActivity
import io.github.feelfreelinux.wykopmobilny.ui.widgets.BadgeDrawerDrawable
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_about_bottomsheet.view.*
import kotlinx.android.synthetic.main.drawer_header_view_layout.view.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject
import android.R.attr.versionName
import android.content.pm.PackageInfo
import android.net.Uri
import io.github.feelfreelinux.wykopmobilny.BuildConfig
import io.github.feelfreelinux.wykopmobilny.api.embed.ExternalApi
import io.github.feelfreelinux.wykopmobilny.base.WykopSchedulers
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.createAlertBuilder


interface MainNavigationInterface {
    val activityToolbar : Toolbar
    fun openFragment(fragment: Fragment)
    fun showErrorDialog(e: Throwable)
    val floatingButton : View
    fun forceRefreshNotifications()
}

class MainNavigationActivity : BaseActivity(), MainNavigationView, NavigationView.OnNavigationItemSelectedListener, MainNavigationInterface {
    override val activityToolbar: Toolbar get() = toolbar
    var tapDoubleClickedMilis = 0L

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
            R.id.login -> { navigator.openLoginScreen(LOGIN_REQUEST_CODE) }
            R.id.messages -> { openFragment(ConversationsListFragment.newInstance()) }
            R.id.nav_settings -> { navigator.openSettingsActivity() }
            R.id.nav_mojwykop -> { openFragment(MyWykopFragment.newInstance()) }
            R.id.nav_home -> { openFragment(PromotedFragment.newInstance()) }
            R.id.search -> { openFragment(SearchFragment.newInstance()) }
            R.id.favourite -> { openFragment(FavoriteFragment.newInstance()) }
            R.id.your_profile -> { startActivity(ProfileActivity.createIntent(this, userManagerApi.getUserCredentials()!!.login)) }
            R.id.nav_wykopalisko -> { openFragment(UpcomingFragment.newInstance()) }
            R.id.hits -> { openFragment(HitsFragment.newInstance()) }
            R.id.about -> { openAboutSheet() }
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
    @Inject lateinit var navigator : NewNavigatorApi
    @Inject lateinit var userManagerApi : UserManagerApi


    private val navHeader by lazy { navigationView.getHeaderView(0) }
    private val actionBarToggle by lazy {
        ActionBarDrawerToggle(this,
                drawer_layout,
                toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_closed)
    }

    private val badgeDrawable by lazy {
        BadgeDrawerDrawable(supportActionBar!!.themedContext)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)
        if (!isTaskRoot) {
            finish()
            return
        }
        presenter.subscribe(this)

        JobUtil.hasBootPermission(this)

        (navigationView.getChildAt(0) as NavigationMenuView).isVerticalScrollBarEnabled = false
        //Setup AppUpdater
        if (BuildConfig.IS_WEEKLY == "true") {
            AppUpdater(this)
                    .setUpdateFrom(UpdateFrom.GITHUB)
                    .setGitHubUserAndRepo("feelfreelinux", "WykopMobilny")
                    .setTitleOnUpdateAvailable(R.string.update_available)
                    .setContentOnUpdateAvailable(R.string.update_app)
                    .setButtonDismiss(R.string.cancel)
                    .setButtonDoNotShowAgain(R.string.do_not_show_again)
                    .setButtonUpdate(R.string.update)
                    .start()
        }
        else {
            AppUpdater(this)
                    .setUpdateFrom(UpdateFrom.GITHUB)
                    .setGitHubUserAndRepo("feelfreelinux", "WykopMobilnyWeekly")
                    .setTitleOnUpdateAvailable(R.string.update_available)
                    .setContentOnUpdateAvailable(R.string.update_app)
                    .setButtonDismiss(R.string.cancel)
                    .setButtonDoNotShowAgain(R.string.do_not_show_again)
                    .setButtonUpdate(R.string.update)
                    .start()
        }
        if (settingsApi.showNotifications) {
            // Schedules notification service
            WykopNotificationsJob.schedule(settingsApi)
        }
        actionBarToggle.drawerArrowDrawable = badgeDrawable
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

    override fun onResume() {
        super.onResume()
        if (!presenter.isSubscribed) {
            presenter.subscribe(this)
            presenter.startListeningForNotifications()
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    private fun setupNavigation() {
        drawer_layout.addDrawerListener(actionBarToggle)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun showUsersMenu(value : Boolean) {
        navigationView.menu.apply {
            setGroupVisible(R.id.nav_user, value)
            findItem(R.id.nav_mojwykop).isVisible = value
            findItem(R.id.login).isVisible = !value
            findItem(R.id.logout).isVisible = value
        }

        navHeader.view_container.isVisible = value
        navHeader.view_container.apply {
            nav_notifications_tag.setOnClickListener {
                deselectItems()
                navigator.openNotificationsListActivity(NotificationsListActivity.PRESELECT_HASHTAGS)
            }

            nav_notifications.setOnClickListener {
                deselectItems()
                navigator.openNotificationsListActivity(NotificationsListActivity.PRESELECT_NOTIFICATIONS)
            }
        }
    }

    fun openMainFragment() {
        when (settingsApi.defaultScreen!!) {
            "mainpage" -> openFragment(PromotedFragment.newInstance())
            "mikroblog" -> openFragment(HotFragment.newInstance())
            "mywykop" -> openFragment(MyWykopFragment.newInstance())
            "hits" -> openFragment(HitsFragment.newInstance())
        }
    }

    override fun openFragment(fragment: Fragment) {
        supportActionBar?.subtitle = null
        fab.isVisible = false
        fab.setOnClickListener(null)
        fab.isVisible = fragment is BaseNavigationView && userManagerApi.isUserAuthorized()
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        ft.replace(R.id.contentView, fragment)
        ft.commit()
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

    override fun showNotificationsCount(notifications: Int) {
        badgeDrawable.text = if (notifications > 0) notifications.toString() else null
        drawer_layout.view_container?.apply {
            notificationCount = notifications
        }

    }

    override fun showHashNotificationsCount(hashNotifications: Int) {
        drawer_layout.view_container?.apply {
            hashTagsNotificationsCount = hashNotifications
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) closeDrawer()
        else {
            if (tapDoubleClickedMilis + 2000L > System.currentTimeMillis()) {
                super.onBackPressed()
                return
            } else Toast.makeText(this, R.string.doubleback_to_exit, Toast.LENGTH_SHORT).show()
            tapDoubleClickedMilis = System.currentTimeMillis()
        }
    }

    override fun restartActivity() {
        navigator.openMainActivity()
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
                if (!presenter.isSubscribed) {
                    presenter.subscribe(this)
                    presenter.startListeningForNotifications()
                }
                presenter.checkNotifications(true)
            }

            else -> {
                if (resultCode == SettingsActivity.THEME_CHANGED_RESULT) {
                    restartActivity()
                }
            }
        }
    }

    fun openAboutSheet() {
        val dialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.app_about_bottomsheet, null)
        dialog.setContentView(bottomSheetView)

        bottomSheetView.apply {
            val versionName = packageManager.getPackageInfo(packageName, 0).versionName
            app_version_textview.text = getString(R.string.app_version, versionName)
            app_version.setOnClickListener {
                openBrowser("https://github.com/feelfreelinux/WykopMobilny")
                dialog.dismiss()
            }

            app_report_bug.setOnClickListener {
                navigator.openTagActivity("owmbugi")
                dialog.dismiss()
            }

            app_observe_tag.setOnClickListener {
                navigator.openTagActivity("otwartywykopmobilny")
                dialog.dismiss()
            }

            app_donate.setOnClickListener {
                navigator.openBrowser("https://paypal.me/WykopMobilny")
                dialog.dismiss()
            }

            license.setOnClickListener {
                openBrowser("https://github.com/feelfreelinux/WykopMobilny/blob/master/LICENSE")
                dialog.dismiss()
            }
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }

    override fun forceRefreshNotifications() {
        presenter.checkNotifications(true)
    }

    override fun checkUpdate(wykopMobilnyUpdate: WykopMobilnyUpdate) {
        val pInfo = this.packageManager.getPackageInfo(packageName, 0)
        val owmVersion = if (wykopMobilnyUpdate.tagName.contains("untagged")) wykopMobilnyUpdate.name else wykopMobilnyUpdate.tagName
        printout(pInfo.versionName)
        if (versionCompare(owmVersion, pInfo.versionName) == 1) {
            createAlertBuilder()
                    .setTitle(R.string.update_available)
                    .setMessage("Aktualizacja $owmVersion jest dostępna.")
                    .setPositiveButton("Pobierz nową wersje", { _, _ ->
                        printout(wykopMobilnyUpdate.assets[0].browserDownloadUrl)
                        val intentUri = Uri.parse(wykopMobilnyUpdate.assets[0].browserDownloadUrl)
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.data = intentUri
                        startActivity(intent)
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
        }

    }

    fun versionCompare(str1: String, str2: String): Int {
        val vals1 = str1.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val vals2 = str2.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var i = 0
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.size && i < vals2.size && vals1[i] == vals2[i]) {
            i++
        }
        // compare first non-equal ordinal number
        if (i < vals1.size && i < vals2.size) {
            val diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]))
            return Integer.signum(diff)
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.size - vals2.size)
    }
}