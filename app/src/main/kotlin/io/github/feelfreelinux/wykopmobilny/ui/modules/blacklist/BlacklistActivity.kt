package io.github.feelfreelinux.wykopmobilny.ui.modules.blacklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.CookieManager
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.scraper.Blacklist
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferences
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_blacklist.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class BlacklistActivity : BaseActivity(), BlacklistView {
    override val enableSwipeBackLayout = true

    val updateDataSubject = PublishSubject.create<Boolean>()

    companion object {
        fun createIntent(context : Context) : Intent {
            return Intent(context, BlacklistActivity::class.java)
        }
    }

    @Inject lateinit var blacklistPreferences : BlacklistPreferences

    override fun importBlacklist(blacklist: Blacklist) {
        blacklistPreferences.blockedTags = HashSet<String>(blacklist.tags.blockedTags.map { it.tag.removePrefix("#") })

        blacklistPreferences.blockedUsers = HashSet<String>(blacklist.users.blockedUsers.map { it.nick.removePrefix("@") })

        updateDataSubject.onNext(true)
    }

    @Inject lateinit var presenter : BlacklistPresenter

    val session by lazy { CookieManager.getInstance().getCookie("https://wykop.pl") }
    val pagerAdapter by lazy { BlacklistPagerAdapter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blacklist)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Zarządzaj czarną listą"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        presenter.subscribe(this)
        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager)
    }

    fun unblockTag(tag : String) {
        presenter.unblockTag(tag)
    }

    fun unblockUser(user : String) {
        presenter.unblockUser(user)
    }

    fun blockTag(tag : String) {
        presenter.blockTag(tag)
    }

    fun blockUser(user : String) {
        presenter.blockUser(user)
    }

    override fun refreshResults() {
        updateDataSubject.onNext(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.blacklist_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sync -> presenter.importBlacklist(session)
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}