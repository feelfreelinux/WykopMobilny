package io.github.feelfreelinux.wykopmobilny.ui.modules.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ProfileResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.actions.ActionsFragment
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.profile_options_bottomsheet.*


class ProfileActivity : BaseActivity(), ProfileView {
    val username by lazy { intent.getStringExtra(EXTRA_USERNAME) }
    @Inject lateinit var navigator : NavigatorApi
    @Inject lateinit var presenter : ProfilePresenter
    @Inject lateinit var userManagerApi : UserManagerApi
    val pagerAdapter by lazy { ProfilePagerAdapter(resources, supportFragmentManager) }
    lateinit var dataFragment : DataFragment<ProfileResponse>

    companion object {
        val EXTRA_USERNAME = "EXTRA_USERNAME"
        val DATA_FRAGMENT_TAG = "PROFILE_DATAFRAGMENT"

        fun createIntent(context : Context, username : String): Intent {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, username)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        presenter.subscribe(this)
        presenter.userName = username
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setBackgroundResource(R.drawable.gradient_toolbar_up)
        tabLayout.setupWithViewPager(pager)
        if (dataFragment.data != null) {
            showProfile(dataFragment.data!!)
        } else {
            presenter.loadData()
            loadingView.isVisible = true
            appBarLayout.isVisible = true
        }
    }

    override fun showProfile(profileResponse: ProfileResponse) {
        dataFragment.data = profileResponse
        pager.offscreenPageLimit = 2
        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager)
        profilePicture.loadImage(profileResponse.avatar)
        nickname.text = profileResponse.login
        nickname.setTextColor(getGroupColor(profileResponse.color))
        loadingView.isVisible = false
        description.isVisible = profileResponse.description != null
        profileResponse.description?.let {
            description.isVisible = true
            description.text = profileResponse.description
        }
        backgroundImg.visibility = if (profileResponse.background != null) View.VISIBLE else View.INVISIBLE
        profileResponse.background?.let {
            backgroundImg.isVisible = true
            backgroundImg.loadImage(profileResponse.background)
        }
    }

    fun showOptionsMenu() {
        val dialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.profile_options_bottomsheet, null)
        dialog.setContentView(bottomSheetView)

        bottomSheetView.apply {
            profile_observe.setOnClickListener {
                dialog.dismiss()
            }
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }
}