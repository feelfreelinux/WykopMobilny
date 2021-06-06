package io.github.wykopmobilny.ui.modules.mainnavigation

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.os.postDelayed
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import io.github.wykopmobilny.R
import io.github.wykopmobilny.api.patrons.PatronsApi
import io.github.wykopmobilny.base.BaseActivity
import io.github.wykopmobilny.base.BaseNavigationView
import io.github.wykopmobilny.blacklist.api.Blacklist
import io.github.wykopmobilny.databinding.ActivityNavigationBinding
import io.github.wykopmobilny.databinding.AppAboutBottomsheetBinding
import io.github.wykopmobilny.databinding.DrawerHeaderViewLayoutBinding
import io.github.wykopmobilny.databinding.PatronListItemBinding
import io.github.wykopmobilny.databinding.PatronsBottomsheetBinding
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.ui.dialogs.confirmationDialog
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.ui.modules.favorite.FavoriteFragment
import io.github.wykopmobilny.ui.modules.links.hits.HitsFragment
import io.github.wykopmobilny.ui.modules.links.promoted.PromotedFragment
import io.github.wykopmobilny.ui.modules.links.upcoming.UpcomingFragment
import io.github.wykopmobilny.ui.modules.loginscreen.LoginScreenActivity
import io.github.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragment
import io.github.wykopmobilny.ui.modules.mywykop.MyWykopFragment
import io.github.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsJob
import io.github.wykopmobilny.ui.modules.notificationslist.NotificationsListActivity
import io.github.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListFragment
import io.github.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListFragment
import io.github.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.wykopmobilny.ui.modules.search.SearchFragment
import io.github.wykopmobilny.ui.modules.settings.SettingsActivity
import io.github.wykopmobilny.ui.widgets.BadgeDrawerDrawable
import io.github.wykopmobilny.ui.widgets.drawerheaderview.DrawerHeaderWidget
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi
import io.github.wykopmobilny.utils.openBrowser
import io.github.wykopmobilny.utils.shortcuts.ShortcutsDispatcher
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.utils.viewBinding
import javax.inject.Inject

interface MainNavigationInterface {
    val activityToolbar: Toolbar
    fun openFragment(fragment: Fragment)
    val floatingButton: View
    fun forceRefreshNotifications()
}

class MainNavigationActivity :
    BaseActivity(),
    MainNavigationView,
    NavigationView.OnNavigationItemSelectedListener,
    MainNavigationInterface {

    companion object {
        const val LOGIN_REQUEST_CODE = 142
        const val TARGET_FRAGMENT_KEY = "TARGET_FRAGMENT"
        const val TARGET_NOTIFICATIONS = "TARGET_NOTIFICATIONS"

        fun getIntent(context: Context, targetFragment: String? = null): Intent {
            val intent = Intent(context, MainNavigationActivity::class.java)
            targetFragment?.let {
                intent.putExtra(TARGET_FRAGMENT_KEY, targetFragment)
            }
            return intent
        }
    }

    @Inject
    lateinit var patronsApi: PatronsApi

    @Inject
    lateinit var blacklistPreferencesApi: BlacklistPreferencesApi

    @Inject
    lateinit var settingsPreferencesApi: SettingsPreferencesApi

    private val binding by viewBinding(ActivityNavigationBinding::inflate)

    override val activityToolbar: Toolbar get() = binding.toolbar.toolbar
    var tapDoubleClickedMillis = 0L

    private val navHeader by lazy { binding.navigationView.getHeaderView(0) as DrawerHeaderWidget }
    private val navHeaderBinding by lazy { DrawerHeaderViewLayoutBinding.bind(navHeader) }
    private val actionBarToggle by lazy {
        ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar.toolbar,
            R.string.nav_drawer_open,
            R.string.nav_drawer_closed,
        )
    }
    private val badgeDrawable by lazy { BadgeDrawerDrawable(supportActionBar!!.themedContext) }
    private val progressDialog by lazy { ProgressDialog(this) }

    override val floatingButton: View
        get() = binding.fab

    @Inject
    lateinit var presenter: MainNavigationPresenter

    @Inject
    lateinit var settingsApi: SettingsPreferencesApi

    @Inject
    lateinit var shortcutsDispatcher: ShortcutsDispatcher

    @Inject
    lateinit var navigator: NewNavigatorApi

    @Inject
    lateinit var userManagerApi: UserManagerApi

    @Inject
    lateinit var linkHandler: WykopLinkHandlerApi

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_mikroblog -> openFragment(HotFragment.newInstance())
            R.id.login -> openLoginScreen()
            R.id.messages -> openFragment(ConversationsListFragment.newInstance())
            R.id.nav_settings -> navigator.openSettingsActivity()
            R.id.nav_mojwykop -> openFragment(MyWykopFragment.newInstance())
            R.id.nav_home -> openFragment(PromotedFragment.newInstance())
            R.id.search -> openFragment(SearchFragment.newInstance())
            R.id.favourite -> openFragment(FavoriteFragment.newInstance())
            R.id.your_profile -> startActivity(ProfileActivity.createIntent(this, userManagerApi.getUserCredentials()!!.login))
            R.id.nav_wykopalisko -> openFragment(UpcomingFragment.newInstance())
            R.id.hits -> openFragment(HitsFragment.newInstance())
            R.id.about -> openAboutSheet()
            R.id.logout -> {
                confirmationDialog(this) {
                    blacklistPreferencesApi.blockedTags = emptySet()
                    blacklistPreferencesApi.blockedUsers = emptySet()
                    userManagerApi.logoutUser()
                    restartActivity()
                }.show()
            }
            else -> showNotImplementedToast()
        }

        item.isChecked = true
        binding.drawerLayout.closeDrawers()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar.toolbar)

        if (!presenter.isSubscribed) {
            presenter.subscribe(this)
        }

        Handler(Looper.getMainLooper()).postDelayed(333) {
            presenter.startListeningForNotifications()
        }

        showFullReleaseDialog()
        (binding.navigationView.getChildAt(0) as NavigationMenuView).isVerticalScrollBarEnabled = false
        checkBlacklist()

        if (settingsApi.showNotifications) {
            // Schedules notification service
            WykopNotificationsJob.schedule(settingsApi)
        }
        actionBarToggle.drawerArrowDrawable = badgeDrawable
        binding.toolbar.toolbar.tag = binding.toolbar.toolbar.overflowIcon // We want to save original overflow icon drawable into memory.
        navHeader.showDrawerHeader(userManagerApi.isUserAuthorized(), userManagerApi.getUserCredentials())
        showUsersMenu(userManagerApi.isUserAuthorized())

        if (savedInstanceState == null) {
            if (intent.hasExtra(TARGET_FRAGMENT_KEY)) {
                when (intent.getStringExtra(TARGET_FRAGMENT_KEY)) {
                    TARGET_NOTIFICATIONS -> openFragment(NotificationsListFragment.newInstance())
                }
            } else openMainFragment()
        }
        setupNavigation()
        shortcutsDispatcher.dispatchIntent(
            intent,
            this::openFragment,
            this::openLoginScreen,
            userManagerApi.isUserAuthorized(),
        )
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (!presenter.isSubscribed) {
            presenter.subscribe(this)

            Handler(Looper.getMainLooper()).postDelayed(333) {
                presenter.startListeningForNotifications()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (presenter.isSubscribed) presenter.unsubscribe()
    }

    private fun setupNavigation() {
        binding.drawerLayout.addDrawerListener(actionBarToggle)
        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    override fun showUsersMenu(value: Boolean) {
        binding.navigationView.menu.apply {
            setGroupVisible(R.id.nav_user, value)
            findItem(R.id.nav_mojwykop).isVisible = value
            findItem(R.id.login).isVisible = !value
            findItem(R.id.logout).isVisible = value
        }

        navHeader.isVisible = value
        navHeader.apply {
            navHeaderBinding.navNotificationsTag.setOnClickListener {
                deselectItems()
                navigator.openNotificationsListActivity(NotificationsListActivity.PRESELECT_HASHTAGS)
            }

            navHeaderBinding.navNotifications.setOnClickListener {
                deselectItems()
                navigator.openNotificationsListActivity(NotificationsListActivity.PRESELECT_NOTIFICATIONS)
            }
        }
    }

    private fun openMainFragment() {
        when (settingsApi.defaultScreen) {
            "mainpage", null -> openFragment(PromotedFragment.newInstance())
            "mikroblog" -> openFragment(HotFragment.newInstance())
            "mywykop" -> openFragment(MyWykopFragment.newInstance())
            "hits" -> openFragment(HitsFragment.newInstance())
        }
    }

    override fun openFragment(fragment: Fragment) {
        supportActionBar?.subtitle = null
        binding.fab.isVisible = false
        binding.fab.setOnClickListener(null)
        binding.fab.isVisible = fragment is BaseNavigationView && userManagerApi.isUserAuthorized()
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        ft.replace(R.id.contentView, fragment)
        ft.commit()
        closeDrawer()
    }

    private fun closeDrawer() = binding.drawerLayout.closeDrawers()

    private fun deselectItems() {
        val menu = binding.navigationView.menu
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
    }

    override fun showNotificationsCount(notifications: Int) {
        badgeDrawable.text = if (notifications > 0) notifications.toString() else null
        navHeader.notificationCount = notifications
    }

    override fun showHashNotificationsCount(hashNotifications: Int) {
        navHeader.hashTagsNotificationsCount = hashNotifications
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) closeDrawer()
        else {
            if (settingsApi.disableExitConfirmation || tapDoubleClickedMillis + 2000L > System.currentTimeMillis()) {
                super.onBackPressed()
                return
            } else Toast.makeText(this, R.string.doubleback_to_exit, Toast.LENGTH_SHORT).show()
            tapDoubleClickedMillis = System.currentTimeMillis()
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
        super.onActivityResult(requestCode, resultCode, data)
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

    private fun openAboutSheet() {
        val dialog = BottomSheetDialog(this)
        val bottomSheetView = AppAboutBottomsheetBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetView.root)

        bottomSheetView.apply {
            val versionName = packageManager.getPackageInfo(packageName, 0).versionName
            appVersionTextview.text = getString(R.string.app_version, versionName)
            appVersion.setOnClickListener {
                openBrowser("https://github.com/feelfreelinux/WykopMobilny")
                dialog.dismiss()
            }

            appReportBug.setOnClickListener {
                navigator.openTagActivity("owmbugi")
                dialog.dismiss()
            }

            appObserveTag.setOnClickListener {
                navigator.openTagActivity("otwartywykopmobilny")
                dialog.dismiss()
            }

            appPatrons.setOnClickListener {
                dialog.dismiss()
                showAppPatronsDialog()
            }

            license.setOnClickListener {
                openBrowser("https://github.com/feelfreelinux/WykopMobilny/blob/master/LICENSE")
                dialog.dismiss()
            }
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.root.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.root.height
        }
        dialog.show()
    }

    private fun showAppPatronsDialog() {
        val dialog2 = BottomSheetDialog(this)
        val badgesDialogView2 = PatronsBottomsheetBinding.inflate(layoutInflater)
        dialog2.setContentView(badgesDialogView2.root)

        val headerItem = PatronListItemBinding.inflate(layoutInflater)
        headerItem.root.setOnClickListener {
            dialog2.dismiss()
            linkHandler.handleUrl("https://patronite.pl/wykop-mobilny")
        }
        headerItem.nickname.text = badgesDialogView2.root.context.getString(R.string.support_app)
        headerItem.tierTextView.text = badgesDialogView2.root.context.getString(R.string.became_patron)
        badgesDialogView2.patronsList.addView(headerItem.root)
        for (badge in patronsApi.patrons.filter { patron -> patron.listMention }) {
            val item = PatronListItemBinding.inflate(layoutInflater)
            item.root.setOnClickListener {
                dialog2.dismiss()
                linkHandler.handleUrl("https://wykop.pl/ludzie/" + badge.username)
            }
            item.nickname.text = badge.username
            item.tierTextView.text = when (badge.tier) {
                "patron50" -> "Patron próg \"Białkowy\""
                "patron25" -> "Patron próg \"Bordowy\""
                "patron10" -> "Patron próg \"Pomaranczowy\""
                "patron5" -> "Patron próg \"Zielony\""
                else -> "Patron"
            }
            badgesDialogView2.patronsList.addView(item.root)
        }
        dialog2.show()
    }

    override fun forceRefreshNotifications() {
        presenter.checkNotifications(true)
    }

    fun showFullReleaseDialog() {
        if (!settingsPreferencesApi.dialogShown) {
            val message = SpannableString(
                "Po prawie dwóch latach pracy publikuję wersję 1.0 aplikacji. " +
                    "Jestem wdzięczny wszystkim osobom zaangażowanym w projekt. " +
                    "Aplikacja nadal pozostaje całkowicie darmowa i wolna od reklam. " +
                    "Jeżeli chcesz, możesz wesprzeć rozwój aplikacji na https://patronite.pl/wykop-mobilny \n" +
                    "Dziękuję :)",
            )
            Linkify.addLinks(message, Linkify.WEB_URLS)
            AlertDialog.Builder(this).apply {
                setTitle("Wersja 1.0")
                setMessage(message)
                setPositiveButton(android.R.string.ok) { _, _ ->
                    settingsPreferencesApi.dialogShown = true
                }

                val dialog = create()
                dialog.setOnDismissListener {
                    settingsPreferencesApi.dialogShown = true
                }
                dialog.show()
                dialog.findViewById<TextView>(android.R.id.message)?.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    override fun importBlacklist(blacklist: Blacklist) {
        if (blacklist.tags?.tags != null) {
            blacklistPreferencesApi.blockedTags = HashSet<String>(blacklist.tags!!.tags!!.map { it.tag.removePrefix("#") })
        }
        if (blacklist.users?.users != null) {
            blacklistPreferencesApi.blockedUsers = HashSet<String>(blacklist.users!!.users!!.map { it.nick.removePrefix("@") })
        }
        blacklistPreferencesApi.blockedImported = true
        progressDialog.hide()
    }

    private fun checkBlacklist() {
        if (canCheckBlacklist()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.blacklist_import_title))
            builder.setMessage(getString(R.string.blacklist_import_ask))
            builder.setPositiveButton(getString(R.string.blacklist_import_action)) { _, _ ->
                progressDialog.isIndeterminate = true
                progressDialog.setTitle(getString(R.string.blacklist_import_progress))
                progressDialog.show()
                presenter.importBlacklist()
            }
            builder.setNegativeButton(android.R.string.cancel, null)
            builder.setCancelable(false)
            builder.show()
        }
    }

    private fun canCheckBlacklist() =
        userManagerApi.isUserAuthorized() &&
            !blacklistPreferencesApi.blockedImported &&
            blacklistPreferencesApi.blockedUsers.isNullOrEmpty() &&
            blacklistPreferencesApi.blockedTags.isNullOrEmpty()

    private fun openLoginScreen() {
        navigator.openLoginScreen(LOGIN_REQUEST_CODE)
    }
}
