package io.github.feelfreelinux.wykopmobilny.activities

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import io.github.feelfreelinux.wykopmobilny.projectors.NavigationActions
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import io.github.feelfreelinux.wykopmobilny.utils.gone
import io.github.feelfreelinux.wykopmobilny.utils.visible
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.toolbar.*

fun Activity.lauchMainNavigation(data : WykopApiData) {
    val intent = Intent(this, NavigationActivity::class.java)
    intent.putExtra("wamData", data)
    startActivity(intent)
}

class NavigationActivity : WykopActivity() {
    val navActions by lazy{ NavigationActions(this) }
    val navHeader by lazy{ navigationView.getHeaderView(0) }
    val actionBarToggle by lazy { ActionBarDrawerToggle(this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close) }
    lateinit var wam : WykopApiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)
        wam = WykopApiManager(intent.getSerializableExtra("wamData") as WykopApiData, this)
        if(savedInstanceState == null) navActions.setupNavigation()
        else navActions.setupHeader()
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

    var isLoading : Boolean
        get() = loadingView.visibility == View.VISIBLE
        set(shouldShow) {
            if (shouldShow) {
                contentView.gone()
                loadingView.visible()
            } else {
                contentView.visible()
                loadingView.gone()
            }
        }

    var isRefreshing : Boolean
        get() = swiperefresh.isRefreshing
        set(value) { swiperefresh.isRefreshing = value }

    fun setSwipeRefreshListener(swipeListener : SwipeRefreshLayout.OnRefreshListener) {
        swiperefresh.setOnRefreshListener(swipeListener)
    }
}