package io.github.feelfreelinux.wykopmobilny.ui.modules.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.patrons.PatronsApi
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.drawBadge
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObserveStateResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ProfileResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.patrons.PatronBadge
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.BadgeResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.api.getGenderStripResource
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.toDurationPrettyDate
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.badge_list_item.view.*
import kotlinx.android.synthetic.main.badges_bottomsheet.view.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class ProfileActivity : BaseActivity(), ProfileView {

    companion object {
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
        const val DATA_FRAGMENT_TAG = "PROFILE_DATAFRAGMENT"

        fun createIntent(context: Context, username: String) =
            Intent(context, ProfileActivity::class.java).apply {
                putExtra(EXTRA_USERNAME, username)
            }
    }

    @Inject lateinit var navigator: NewNavigatorApi
    @Inject lateinit var presenter: ProfilePresenter
    @Inject lateinit var userManagerApi: UserManagerApi
    @Inject lateinit var patronsApi: PatronsApi

    val username by lazy { intent.getStringExtra(EXTRA_USERNAME) }
    override val enableSwipeBackLayout: Boolean = true
    private var observeStateResponse: ObserveStateResponse? = null
    private lateinit var badgesDialogListener: (List<BadgeResponse>) -> Unit
    private val pagerAdapter by lazy { ProfilePagerAdapter(resources, supportFragmentManager) }

    lateinit var dataFragment: DataFragment<ProfileResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        presenter.subscribe(this)
        presenter.userName = username

        supportActionBar?.apply {
            title = null
            setDisplayHomeAsUpEnabled(true)
        }
        supportActionBar?.title = null

        tabLayout.setupWithViewPager(pager)
        if (dataFragment.data != null) {
            showProfile(dataFragment.data!!)
        } else {
            presenter.loadData()
            loadingView?.isVisible = true
            appBarLayout.isVisible = true
        }
    }

    fun getBadgeFor(nick: String): PatronBadge? {
        return try {
            patronsApi.patrons.firstOrNull { it.username == nick }?.badge
        } catch (e: Throwable) {
            null
        }
    }

    override fun showProfile(profileResponse: ProfileResponse) {
        dataFragment.data = profileResponse
        pager.offscreenPageLimit = 2
        pager.adapter = pagerAdapter
        getBadgeFor(profileResponse.login)?.apply {
            drawBadge(patronBadgeTextView)
        }
        tabLayout.setupWithViewPager(pager)
        profilePicture.loadImage(profileResponse.avatar)
        signup.text = profileResponse.signupAt.toDurationPrettyDate()
        nickname.text = profileResponse.login
        nickname.setTextColor(getGroupColor(profileResponse.color))
        loadingView?.isVisible = false
        description.isVisible = profileResponse.description != null
        profileResponse.description?.let {
            description.isVisible = true
            description.text = profileResponse.description
        }
        profileResponse.isObserved?.let {
            observeStateResponse = ObserveStateResponse(profileResponse.isObserved, profileResponse.isBlocked!!)
            invalidateOptionsMenu()
        }
        if (profileResponse.followers != 0) {
            followers.isVisible = true
            followers.text = getString(R.string.followers, dataFragment.data!!.followers)
        }
        if (profileResponse.rank != 0) {
            rank.isVisible = true
            rank.text = "#${profileResponse.rank}"
            rank.setBackgroundColor(getGroupColor(profileResponse.color))
        }
        profileResponse.sex?.let {
            genderStripImageView.isVisible = true
            genderStripImageView.setBackgroundResource(getGenderStripResource(profileResponse.sex!!))
        }

        profileResponse.ban?.apply {
            if (profileResponse.ban?.reason != null && profileResponse.ban?.date != null) {
                banTextView.isVisible = true
                banTextView.text = "Użytkownik zbanowany do $date za $reason"
            }
        }
        backgroundImg.isVisible = true
        profileResponse.background?.let {
            backgroundImg.loadImage(profileResponse.background!!)
            toolbar.setBackgroundResource(R.drawable.gradient_toolbar_up)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        if (userManagerApi.isUserAuthorized() && userManagerApi.getUserCredentials()!!.login != username) {
            menu.findItem(R.id.pw).isVisible = true
            observeStateResponse?.apply {
                menu.apply {
                    findItem(R.id.unobserve_profile).isVisible = isObserved
                    findItem(R.id.observe_profile).isVisible = !isObserved
                    findItem(R.id.block).isVisible = !isBlocked
                    findItem(R.id.unblock).isVisible = isBlocked
                }
            }
        }
        return true
    }

    override fun showButtons(observeState: ObserveStateResponse) {
        observeStateResponse = observeState
        invalidateOptionsMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pw -> dataFragment.data?.let { navigator.openConversationListActivity(dataFragment.data!!.login) }
            R.id.unblock -> {
                presenter.markUnblocked()
            }
            R.id.block -> {
                presenter.markBlocked()
            }
            R.id.observe_profile -> {
                presenter.markObserved()
            }
            R.id.unobserve_profile -> {
                presenter.markUnobserved()
            }
            R.id.badges -> {
                showBadgesDialog()
            }
            R.id.report -> {
                navigator.openReportScreen(dataFragment.data!!.violationUrl!!)
            }
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun showBadgesDialog() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val badgesDialogView = layoutInflater.inflate(R.layout.badges_bottomsheet, null)
        badgesDialogView.badgesList
        dialog.setContentView(badgesDialogView)
        badgesDialogListener = {
            if (dialog.isShowing) {
                badgesDialogView.loadingView?.isVisible = false
                for (badge in it) {
                    val item = layoutInflater.inflate(R.layout.badge_list_item, null)
                    item.description.text = badge.description
                    item.date.text = badge.date.toPrettyDate()
                    item.badgeTitle.text = badge.name
                    item.badgeImg.loadImage(badge.icon)
                    badgesDialogView.badgesList.addView(item)
                }
            }
        }
        dialog.show()
        presenter.getBadges()
    }

    override fun showBadges(badges: List<BadgeResponse>) = badgesDialogListener(badges)

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }
}