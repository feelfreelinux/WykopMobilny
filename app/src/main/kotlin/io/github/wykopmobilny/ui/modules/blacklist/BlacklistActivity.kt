package io.github.wykopmobilny.ui.modules.blacklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseActivity
import io.github.wykopmobilny.databinding.ActivityBlacklistBinding
import io.github.wykopmobilny.models.scraper.Blacklist
import io.github.wykopmobilny.utils.preferences.BlacklistPreferences
import io.github.wykopmobilny.utils.viewBinding
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class BlacklistActivity : BaseActivity(), BlacklistView {

    companion object {
        fun createIntent(context: Context) = Intent(context, BlacklistActivity::class.java)
    }

    @Inject
    lateinit var blacklistPreferences: BlacklistPreferences

    @Inject
    lateinit var presenter: BlacklistPresenter

    val updateDataSubject = PublishSubject.create<Boolean>()
    override val enableSwipeBackLayout = true
    private val pagerAdapter by lazy { BlacklistPagerAdapter(supportFragmentManager) }
    private val binding by viewBinding(ActivityBlacklistBinding::inflate)

    override fun importBlacklist(blacklist: Blacklist) {
        if (blacklist.tags?.blockedTags != null) {
            blacklistPreferences.blockedTags = HashSet<String>(blacklist.tags!!.blockedTags!!.map { it.tag.removePrefix("#") })
        }
        if (blacklist.users?.blockedUsers != null) {
            blacklistPreferences.blockedUsers = HashSet<String>(blacklist.users!!.blockedUsers!!.map { it.nick.removePrefix("@") })
        }
        blacklistPreferences.blockedImported = true
        updateDataSubject.onNext(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.title = "Zarządzaj czarną listą"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter.subscribe(this)
        binding.pager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.pager)
    }

    fun unblockTag(tag: String) = presenter.unblockTag(tag)

    fun unblockUser(user: String) = presenter.unblockUser(user)

    fun blockTag(tag: String) = presenter.blockTag(tag)

    fun blockUser(user: String) = presenter.blockUser(user)

    override fun refreshResults() = updateDataSubject.onNext(true)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.blacklist_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sync -> presenter.importBlacklist()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }
}
