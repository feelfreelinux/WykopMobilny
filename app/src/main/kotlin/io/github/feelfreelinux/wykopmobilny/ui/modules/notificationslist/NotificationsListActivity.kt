package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.databinding.ActivityNotificationsBinding
import io.github.feelfreelinux.wykopmobilny.utils.viewBinding

class NotificationsListActivity : BaseActivity() {

    companion object {
        const val EXTRA_PRESELECT_INDEX = "PRESELECT_INDEX"
        const val PRESELECT_NOTIFICATIONS = 0
        const val PRESELECT_HASHTAGS = 1

        fun createIntent(context: Context, currentIndex: Int = PRESELECT_NOTIFICATIONS) =
            Intent(context, NotificationsListActivity::class.java).apply {
                putExtra(EXTRA_PRESELECT_INDEX, currentIndex)
            }
    }

    private val binding by viewBinding(ActivityNotificationsBinding::inflate)

    override val enableSwipeBackLayout: Boolean = true
    private val pagerAdapter by lazy { NotificationsListViewPagerAdapter(resources, supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.setTitle(R.string.notifications_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.pager.offscreenPageLimit = 1
        binding.pager.adapter = pagerAdapter
        binding.pager.setCurrentItem(intent.getIntExtra(EXTRA_PRESELECT_INDEX, 0), false)
        binding.tabLayout.setupWithViewPager(binding.pager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.notification_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.markAsRead -> {
                (pagerAdapter.registeredFragments.get(binding.pager.currentItem) as? BaseNotificationsListFragment)?.markAsRead()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
