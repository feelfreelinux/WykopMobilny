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
import kotlinx.android.synthetic.main.activity_blacklist.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class BlacklistActivity : BaseActivity(), BlacklistView {
    companion object {
        fun createIntent(context : Context) : Intent {
            return Intent(context, BlacklistActivity::class.java)
        }
    }

    @Inject lateinit var blacklistPreferences : BlacklistPreferences

    override fun importBlacklist(blacklist: Blacklist) {
        val tagsSet = HashSet<String>(blacklistPreferences.blockedTags)
        blacklist.tags.blockedTags.forEach {
            if (!tagsSet.contains(it.tag)) {
                tagsSet.add(it.tag)
            }
        }
        blacklistPreferences.blockedTags = tagsSet

        val usersSet = HashSet<String>(blacklistPreferences.blockedUsers)
        blacklist.users.blockedUsers.forEach {
            if (!usersSet.contains(it.nick)) {
                usersSet.add(it.nick)
            }
        }
        blacklistPreferences.blockedUsers = usersSet
    }

    @Inject lateinit var presenter : BlacklistPresenter

    val session by lazy { CookieManager.getInstance().getCookie("https://wykop.pl") }
    val pagerAdapter by lazy { BlacklistPagerAdapter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blacklist)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Zarządzaj czarną listą"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        presenter.subscribe(this)
        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager)
    }

    fun unlockTag(tag : String) {}

    fun unlockUser(user : String) {

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
}