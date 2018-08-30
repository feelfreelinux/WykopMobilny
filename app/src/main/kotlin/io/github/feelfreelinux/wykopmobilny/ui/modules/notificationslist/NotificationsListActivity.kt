package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.toolbar.*


class NotificationsListActivity : BaseActivity() {
    override val enableSwipeBackLayout: Boolean = true
    companion object {
        val EXTRA_PRESELECT_INDEX = "PRESELECT_INDEX"
        val PRESELECT_NOTIFICATIONS = 0
        val PRESELECT_HASHTAGS = 1
        fun createIntent(context : Context, currentIndex : Int = PRESELECT_NOTIFICATIONS) : Intent {
            val intent = Intent(context, NotificationsListActivity::class.java)
            intent.putExtra(EXTRA_PRESELECT_INDEX, currentIndex)
            return intent
        }
    }
    val pagerAdapter by lazy { NotificationsListViewPagerAdapter(resources, supportFragmentManager) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.notifications_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        pager.offscreenPageLimit = 1
        pager.adapter = pagerAdapter
        pager.setCurrentItem(intent.getIntExtra(EXTRA_PRESELECT_INDEX, 0), false)
        tabLayout.setupWithViewPager(pager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.notification_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.markAsRead -> {
                (pagerAdapter.registeredFragments.get(pager.currentItem) as? BaseNotificationsListFragment)?.markAsRead()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}